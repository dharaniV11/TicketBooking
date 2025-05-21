package com.example.ticketbooking.RequestBean;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookingRequestBean {
    private UUID id;
    private String Email;
    private String movieName;
    private String theaterName;
    private LocalDateTime showTime;
    private Integer seatCount;
    private UUID movieShowDetailsId;
    private BigDecimal totalAmount;
//    private Boolean ispaid;
}
