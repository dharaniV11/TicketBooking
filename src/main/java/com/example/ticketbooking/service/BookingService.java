package com.example.ticketbooking.service;

import com.example.ticketbooking.requestBean.BookingRequestBean;
import com.example.ticketbooking.responseBean.MovieShowDetailsResponseBean;
import com.example.ticketbooking.exception.NotAvailableException;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.entity.*;
import com.example.ticketbooking.exception.UnableToProcessCauseOfTimeException;
import com.example.ticketbooking.repository.*;
import com.example.ticketbooking.responseBean.BookingResponseBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepository userRepository;

    private final MovieRepository movieRepository;

    private final TheaterRepository theaterRepository;

    private final ShowTimeRepository showTimeRepository;

    private final BookingRepository bookingRepository;

    private final MovieShowDetailsRepository movieShowDetailsRepository;

    //Show All Availabilities
    public List<MovieShowDetailsResponseBean> getAllShowAvailabilities() {
        return getMovieShowDetailsResponseBeans(movieShowDetailsRepository, log);
    }

    //MovieShowDetails Static Method
    static List<MovieShowDetailsResponseBean> getMovieShowDetailsResponseBeans(MovieShowDetailsRepository movieShowDetailsRepository, Logger log) {
        List<MovieShowDetailsEntity> allDetails = movieShowDetailsRepository.findAll();
        List<MovieShowDetailsResponseBean> result = new ArrayList<>();

        for (MovieShowDetailsEntity detail : allDetails) {
            MovieShowDetailsResponseBean dto = new MovieShowDetailsResponseBean(
                    detail.getId(),
                    detail.getMovie().getMovieName(),
                    detail.getTheater().getTheaterName(),
                    detail.getShowTime().getShowTime(),
                    detail.getAvailableSeats()
            );
            result.add(dto);
        }
        log.info("getAllShowAvailabilities result: {}", result.size());
        return result;
    }


    //Create a new booking
    public BookingRequestBean createBooking(BookingRequestBean bookingRequestBean) throws ResourceNotFoundException {

        UserEntity user = userRepository.findByEmail(bookingRequestBean.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        MovieEntity movie = movieRepository.findMovieByName(bookingRequestBean.getMovieName())
                .orElseThrow(() -> new ResourceNotFoundException("Movie Not Found"));

        TheaterEntity theater = theaterRepository.findByName(bookingRequestBean.getTheaterName())
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Found"));

        ShowTimeEntity showTime = showTimeRepository.findByShowTime(bookingRequestBean.getShowTime())
                .orElseThrow(() -> new ResourceNotFoundException("ShowTime Not Found"));

        if (showTime.getShowTime().isBefore(LocalDateTime.now().plusMinutes(30))) {
            log.error("Booking must be done at least 30 minutes before the show time");
            throw new UnableToProcessCauseOfTimeException("Booking must be done at least 30 minutes before the show time");
        }

        MovieShowDetailsEntity showDetails = movieShowDetailsRepository
                .findByMovieAndTheaterAndShowTime(movie, theater, showTime)
                .orElseThrow(() -> new ResourceNotFoundException("Show details not found"));

        Integer seatCount = bookingRequestBean.getSeatCount();
        if (seatCount > showDetails.getAvailableSeats()) {
            log.error("Not enough available seats, Total available seats {}", showDetails.getAvailableSeats());
            throw new NotAvailableException("Not enough available seats, Total available seats " + showDetails.getAvailableSeats());
        }

        BigDecimal totalAmount = movie.getPrice().multiply(BigDecimal.valueOf(seatCount));

        BookingEntity bookingEntity = BookingEntity.builder()
                .Email(bookingRequestBean.getEmail())
                .seatCount(seatCount)
                .totalAmount(totalAmount)
                .movieEntity(movie)
                .theaterEntity(theater)
                .showTimeEntity(showTime)
                .movieShowDetailsEntity(showDetails)
                .build();
        log.info("createBooking result: {}", bookingEntity);
        bookingRepository.save(bookingEntity);

        return BookingRequestBean.builder()
                .id(bookingEntity.getId())
                .Email(user.getEmail())
                .movieName(movie.getMovieName())
                .theaterName(theater.getTheaterName())
                .showTime(showTime.getShowTime())
                .movieShowDetailsId(bookingEntity.getMovieShowDetailsEntity().getId())
                .seatCount(bookingEntity.getSeatCount())
                .totalAmount(bookingEntity.getTotalAmount())
                .build();
    }

    //Payment
    public String processPayment(UUID bookingId, BigDecimal amount) throws ResourceNotFoundException {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (bookingEntity.isPaid()==true){
            log.warn("Booking is already paid");
            return "payment done already";
        }
        else{
            if (bookingEntity.getTotalAmount().compareTo(amount) == 0) {
                bookingEntity.setPaid(true);
                bookingRepository.save(bookingEntity);

                UUID showId = bookingEntity.getMovieShowDetailsEntity().getId();
                MovieShowDetailsEntity showDetails = movieShowDetailsRepository.findById(showId)
                        .orElseThrow(() -> new ResourceNotFoundException("Show details not found"));
                showDetails.setAvailableSeats(showDetails.getAvailableSeats() - bookingEntity.getSeatCount());
                movieShowDetailsRepository.save(showDetails);
                log.info("Booking paid successfully {}", amount);
                return "Payment successful " + amount;
            }

            else {
            bookingEntity.setPaid(false);
            bookingRepository.save(bookingEntity);
            log.error("Payment failed: Expected {}", bookingEntity.getTotalAmount());
            return "Payment failed: Expected " + bookingEntity.getTotalAmount();
            }
        }
    }

    //Cancel Booking
    public String CancelBookingById(@PathVariable UUID bookingId)
    {

        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(()-> new RuntimeException("Booking not found with the given BookingId: " + bookingId));

        if (bookingRepository.findById(bookingId).get().getMovieShowDetailsEntity().getShowTime().getShowTime().isBefore(LocalDateTime.now().plusMinutes(30))) {
            log.error("Canceling ticket must be done at least 30 minutes before the show time");
            throw new UnableToProcessCauseOfTimeException("Canceling ticket must be done at least 30 minutes before the show time");
        }

        if (bookingEntity != null) {
            MovieShowDetailsEntity movieShowDetailsEntity = bookingEntity.getMovieShowDetailsEntity();
                 movieShowDetailsEntity.setAvailableSeats(bookingEntity.getMovieShowDetailsEntity().getAvailableSeats() + bookingEntity.getSeatCount());
            movieShowDetailsRepository.save(movieShowDetailsEntity);
            BigDecimal totalAmount = bookingEntity.getTotalAmount();
            bookingRepository.delete(bookingEntity);
            log.info("Booking deleted successfully \nRefund: {}", totalAmount);
            return "Booking deleted successfully \nRefund: " + totalAmount;
        }
        return null;
    }

    //Generating Ticket
    public byte[] generateTicketFile(UUID bookingId) throws ResourceNotFoundException {
        BookingEntity bookingEntity = bookingRepository.getById(bookingId);
        if (bookingEntity.isPaid()==false) {
            log.error("payment not completed");
            throw new ResourceNotFoundException("payment not completed");
        }

        String ticket = "----- TICKET -----\n"
                + "Email: " + bookingEntity.getEmail() + "\n"
                + "Booking ID: " + bookingEntity.getId() + "\n"
                + "Movie: " + bookingEntity.getMovieEntity().getMovieName() + "\n"
                + "Theater: " + bookingEntity.getTheaterEntity().getTheaterName() + "\n"
                + "Show Time: " + bookingEntity.getShowTimeEntity().getShowTime() + "\n"
                + "Seats: " + bookingEntity.getSeatCount() + "\n"
                + "Amount Paid: ₹" + bookingEntity.getTotalAmount() + "\n"
                + "------------------\n";

        log.info("ticket: {}", ticket.getBytes());
        return ticket.getBytes();
    }

    //Get all bookings
    public List<BookingResponseBean> getAllBookings() {
        List<BookingResponseBean> allBooking = new ArrayList<>();
        List<BookingEntity> bookingEntityList = bookingRepository.findAll();
        for (BookingEntity bookingEntity : bookingEntityList) {
            if (bookingEntity.isPaid()==true) {
                BookingResponseBean bookingResponseBean = BookingResponseBean.builder()
                        .id(bookingEntity.getId())
                        .Email(bookingEntity.getEmail())
                        .MovieName(bookingEntity.getMovieEntity().getMovieName())
                        .TheaterName(bookingEntity.getTheaterEntity().getTheaterName())
                        .ShowTime(bookingEntity.getShowTimeEntity().getShowTime())
                        .SeatCount(bookingEntity.getSeatCount())
                        .TotalAmount(bookingEntity.getTotalAmount())
                        .ispaid(bookingEntity.isPaid())
                        .build();
                allBooking.add(bookingResponseBean);
            }
        }
        log.info("All Booking get successful {}", allBooking.size());
        return allBooking;
    }
}
