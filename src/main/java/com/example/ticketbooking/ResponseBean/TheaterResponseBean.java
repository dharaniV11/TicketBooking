package com.example.ticketbooking.ResponseBean;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TheaterResponseBean {

    private UUID id;
    private String theaterName;
    private Integer totalSeats;

}
