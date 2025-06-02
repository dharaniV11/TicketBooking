package com.example.ticketbooking.responseBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieShowDetailsResponseBean {
        private UUID movieShowDetailsId;
        private String movieName;
        private String theaterName;
        private LocalDateTime showTime;
        private int availableSeats;
    }

