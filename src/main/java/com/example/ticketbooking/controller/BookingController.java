package com.example.ticketbooking.controller;

import com.example.ticketbooking.RequestBean.BookingRequestBean;
import com.example.ticketbooking.RequestBean.MovieShowDetailsRequestBean;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.ResponseBean.BookingResponseBean;
import com.example.ticketbooking.ResponseBean.MovieShowDetailsResponseBean;
import com.example.ticketbooking.service.BookingService;
import com.example.ticketbooking.service.ShowDetailsCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {


    private final BookingService bookingService;

    private final ShowDetailsCriteriaService showDetailsFilterService;

    @GetMapping("/availability")
    public List<MovieShowDetailsResponseBean> getAllShowAvailabilities() {
        return bookingService.getAllShowAvailabilities();
    }

    @GetMapping("/search")
    public List<MovieShowDetailsResponseBean> searchShowDetails(@RequestParam(required = false) String theater,
                                                                @RequestParam(required = false) String movie,
                                                                @RequestParam(required = false) LocalDateTime showTime) throws ResourceNotFoundException {
        return showDetailsFilterService.searchShowDetails(theater,movie,showTime);
    }


    @PostMapping("/bookTicket")
    public ResponseEntity<EntityModel<BookingRequestBean>> bookTicket(@RequestBody BookingRequestBean bookingRequestBean) throws ResourceNotFoundException {

        BookingRequestBean addBooking = bookingService.createBooking(bookingRequestBean);
        EntityModel<BookingRequestBean> resource = EntityModel.of(addBooking,
                linkTo(methodOn(BookingController.class)
                        .makePayment((addBooking.getId()),
                                addBooking.getTotalAmount()))
                        .withRel("payment-link").withType("POST"));

        return ResponseEntity.created(linkTo(methodOn(BookingController.class)
                .makePayment(addBooking.getId(),
                        addBooking.getTotalAmount())).toUri()).body(resource);
    }

    @PostMapping("/payment/{bookingId}")
    public ResponseEntity<String> makePayment(@PathVariable UUID bookingId ,
                                              @RequestParam BigDecimal amount) throws ResourceNotFoundException {
        String result = bookingService.processPayment(bookingId, amount);

        EntityModel<String> resource = EntityModel.of(result,linkTo(methodOn(BookingController.class).downloadTicket(bookingId)).withRel("Download-Ticket"));
        return ResponseEntity.ok(resource.toString());
//        return ResponseEntity.ok(result);
    }

    @GetMapping("/ticket/{bookingId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable UUID bookingId) throws ResourceNotFoundException {
        byte[] ticket = bookingService.generateTicketFile(bookingId);

        return ResponseEntity.ok().body(ticket);
    }

    @DeleteMapping("/deleteBooking/{bookingId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID bookingId) {
        String result = bookingService.DeleteBookingById(bookingId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllBooking")
    public List<BookingResponseBean> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
