package com.example.ticketbooking.responseBean;

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
