package com.example.ticketbooking.ResponseBean;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class BookingResponseBean
{
    private UUID id;
    private String Email;
    private String MovieName;
    private String TheaterName;
    private LocalDateTime ShowTime;
    private Integer SeatCount;
    private BigDecimal TotalAmount;
    private Boolean ispaid;
}

