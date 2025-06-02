package com.example.ticketbooking.requestBean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovieShowDetailsRequestBean {
    private String movieName;
    private String theaterName;
    private LocalDateTime showTime;
    private int availableSeats;
}
