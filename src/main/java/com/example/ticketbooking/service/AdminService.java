package com.example.ticketbooking.service;


import com.example.ticketbooking.requestBean.MovieRequestBean;
import com.example.ticketbooking.requestBean.MovieShowDetailsRequestBean;
import com.example.ticketbooking.requestBean.ShowTimeRequestBean;
import com.example.ticketbooking.requestBean.TheaterRequestBean;
import com.example.ticketbooking.responseBean.MovieShowDetailsResponseBean;
import com.example.ticketbooking.responseBean.ShowTimeResponseBean;
import com.example.ticketbooking.responseBean.TheaterResponseBean;
import com.example.ticketbooking.entity.MovieEntity;
import com.example.ticketbooking.entity.MovieShowDetailsEntity;
import com.example.ticketbooking.entity.ShowTimeEntity;
import com.example.ticketbooking.entity.TheaterEntity;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.exception.NotAvailableException;
import com.example.ticketbooking.repository.*;
import com.example.ticketbooking.responseBean.MovieResponseBean;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.ticketbooking.service.BookingService.getMovieShowDetailsResponseBeans;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MovieRepository movieRepository;

    private final TheaterRepository theaterRepository;

    private final ShowTimeRepository showTimeRepository;

    private final MovieShowDetailsRepository movieShowDetailsRepository;

    public MovieRequestBean addMovie(@Valid @PathVariable MovieRequestBean movieRequestBean) {
        if (movieRequestBean == null) {
            return null;
        }
        MovieEntity movieEntity = MovieEntity.builder()
                .movieName(movieRequestBean.getMovieName())
                .genre(movieRequestBean.getGenre())
                .rating(movieRequestBean.getRating())
                .price(movieRequestBean.getPrice())
                .build();
        movieRepository.save(movieEntity);
        log.info("Movie added successfully {}", movieEntity.getMovieName());
        return movieRequestBean;
    }

    public String deleteMovieById(UUID MovieId) throws ResourceNotFoundException {
        MovieEntity movieEntity = movieRepository.findById(MovieId).orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        if (movieEntity != null) {
            movieRepository.delete(movieEntity);
            log.info("Movie deleted successfully {}", movieEntity.getMovieName());
            return "Movie Deleted Successfully " + movieEntity.getMovieName();
        }
        return null;
    }

    public List<MovieResponseBean> getAllMovies() {
        List<MovieResponseBean> MovieResponseBeans = new ArrayList<>();
        List<MovieEntity> movieEntityList = movieRepository.findAll();
        for (MovieEntity movieEntity : movieEntityList) {
            MovieResponseBean movieResponseBean = MovieResponseBean.builder()
                    .id(movieEntity.getId())
                    .movieName(movieEntity.getMovieName())
                    .genre(movieEntity.getGenre())
                    .rating(movieEntity.getRating())
                    .price(movieEntity.getPrice())
                    .build();
            MovieResponseBeans.add(movieResponseBean);
        }
        log.info("All movies get successfully, total no of movies {}", MovieResponseBeans.size());
        return MovieResponseBeans;
    }

    public TheaterRequestBean addTheater(@Valid @PathVariable TheaterRequestBean theaterRequestBean) {
        if (theaterRequestBean == null) {
            return null;
        }
        TheaterEntity theaterEntity = TheaterEntity.builder()
                .theaterName(theaterRequestBean.getTheaterName())
                .totalSeats(theaterRequestBean.getTotalSeats())
                .build();
        theaterRepository.save(theaterEntity);
        log.info("Theater added successfully {}", theaterEntity.getTheaterName());
        return theaterRequestBean;
    }

    public String deleteTheaterById(UUID TheaterId) throws ResourceNotFoundException {
        TheaterEntity theaterEntity = theaterRepository.findById(TheaterId).orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
        if (theaterEntity != null) {
            theaterRepository.delete(theaterEntity);
            log.info("Theater deleted successfully {}", theaterEntity.getTheaterName());
            return "Theater Deleted Successfully " + theaterEntity.getTheaterName();
        }
        return null;
    }

    public List<TheaterResponseBean> getAllTheater() {
        List<TheaterResponseBean> TheaterResponseBeans = new ArrayList<>();
        List<TheaterEntity> theaterEntityList = theaterRepository.findAll();
        for (TheaterEntity theaterEntity : theaterEntityList) {
            TheaterResponseBean theaterResponseBean = TheaterResponseBean.builder()
                    .id(theaterEntity.getId())
                    .theaterName(theaterEntity.getTheaterName())
                    .totalSeats(theaterEntity.getTotalSeats())
                    .build();
            TheaterResponseBeans.add(theaterResponseBean);
        }
        log.info("All theater get successfully, total no of theaters {}", TheaterResponseBeans.size());
        return TheaterResponseBeans;
    }

    public ShowTimeRequestBean addShowTime(@Valid @PathVariable ShowTimeRequestBean showTimeRequestBean) {
        if (showTimeRequestBean == null) {
            return null;
        }
        ShowTimeEntity showTimeEntity = ShowTimeEntity.builder()
                .showTime(showTimeRequestBean.getShowTime())
                .build();
        showTimeRepository.save(showTimeEntity);
        log.info("ShowTime added successfully {}",showTimeEntity.getShowTime());
        return showTimeRequestBean;
    }

    public String deleteShowTimeById(UUID ShowTimeId) throws ResourceNotFoundException {
        ShowTimeEntity showTimeEntity = showTimeRepository.findById(ShowTimeId).orElseThrow(() -> new ResourceNotFoundException("ShowTime not found"));
        if (showTimeEntity != null) {
            showTimeRepository.delete(showTimeEntity);
            log.info("ShowTime Deleted successfully {}", showTimeEntity.getShowTime());
            return "ShowTime Deleted Successfully " + showTimeEntity.getShowTime();
        }
        return null;
    }

    public List<ShowTimeResponseBean> getAllShowTime() {
        List<ShowTimeResponseBean> ShowTimeResponseBeans = new ArrayList<>();
        List<ShowTimeEntity> showTimeEntityList = showTimeRepository.findAll();
        for (ShowTimeEntity showTimeEntity : showTimeEntityList) {
            ShowTimeResponseBean showTimeResponseBean = ShowTimeResponseBean.builder()
                    .id(showTimeEntity.getId())
                    .showTime(showTimeEntity.getShowTime())
                    .build();
            ShowTimeResponseBeans.add(showTimeResponseBean);
        }
        log.info("All ShowTimes get successfully, total no of ShowTime {}",ShowTimeResponseBeans.size());
        return ShowTimeResponseBeans;
    }

    public String addMovieShowDetails(MovieShowDetailsRequestBean movieShowDetailsRequestBean) throws ResourceNotFoundException {

        TheaterEntity theater = theaterRepository.findByName(movieShowDetailsRequestBean.getTheaterName())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found"));

        ShowTimeEntity showTime = showTimeRepository.findByShowTime(movieShowDetailsRequestBean.getShowTime())
                .orElseThrow(() -> new ResourceNotFoundException("Show time not found"));

        boolean exists = movieShowDetailsRepository.existsByTheaterIdAndShowTimeId(theater.getId(), showTime.getId());
        if (exists) {
            log.warn("A movie is already scheduled in this theater at the selected timeslot");
            throw new NotAvailableException("A movie is already scheduled in this theater at the selected timeslot.");
        }

        MovieEntity movie = movieRepository.findMovieByName(movieShowDetailsRequestBean.getMovieName())
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        MovieShowDetailsEntity details = new MovieShowDetailsEntity();
        details.setTheater(theater);
        details.setMovie(movie);
        details.setShowTime(showTime);
        details.setAvailableSeats(theater.getTotalSeats());

        movieShowDetailsRepository.save(details);
        log.info("{} - {} - {} - Movie show details added successfully", details.getTheater().getTheaterName(), details.getMovie().getMovieName(), details.getShowTime().getShowTime());
        return details.getTheater().getTheaterName() + " - "+ details.getMovie().getMovieName() + " - "+ details.getShowTime().getShowTime() +" - Movie show details added successfully.";
    }

    public String DeleteMovieShowDetails(UUID movieShowDetailsId) throws ResourceNotFoundException {

        MovieShowDetailsEntity movieShowDetailsEntity = movieShowDetailsRepository.findById(movieShowDetailsId).orElseThrow(() -> new ResourceNotFoundException("MovieShowDetails not found"));
        if (movieShowDetailsEntity != null) {
            movieShowDetailsRepository.delete(movieShowDetailsEntity);
            log.info("MovieShowDetails Deleted successfully {} - {} - {}",movieShowDetailsEntity.getMovie().getMovieName(), movieShowDetailsEntity.getTheater().getTheaterName(), movieShowDetailsEntity.getShowTime().getShowTime());
            return "MovieShowDetails Deleted Successfully " + movieShowDetailsEntity.getMovie().getMovieName() +" "+ movieShowDetailsEntity.getTheater().getTheaterName() + " " + movieShowDetailsEntity.getShowTime().getShowTime();
        }
        return null;
    }

    public List<MovieShowDetailsResponseBean> getAllShowAvailabilities() {
        return getMovieShowDetailsResponseBeans(movieShowDetailsRepository, log);
    }
}