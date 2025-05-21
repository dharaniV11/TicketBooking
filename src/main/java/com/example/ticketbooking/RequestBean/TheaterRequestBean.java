package com.example.ticketbooking.RequestBean;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TheaterRequestBean {

    @NotBlank(message = "theaterName cannot be empty")
    private String theaterName;

    @NotNull(message = "totalSeats cannot be null")
    private Integer totalSeats;
}
