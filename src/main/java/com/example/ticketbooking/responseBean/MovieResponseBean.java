package com.example.ticketbooking.responseBean;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseBean {

    private UUID id;
    private String movieName;
    private String genre;
    private double rating;
    private BigDecimal price;
}
