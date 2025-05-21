package com.example.ticketbooking.ResponseBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieShowDetailsResponseBean {
        private String movieName;
        private String theaterName;
        private LocalDateTime showTime;
        private int availableSeats;
    }

