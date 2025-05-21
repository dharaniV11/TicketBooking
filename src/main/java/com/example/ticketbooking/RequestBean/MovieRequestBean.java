package com.example.ticketbooking.RequestBean;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequestBean {

    @NotBlank(message = "Movie name cannot be empty")
    private String movieName;

    @NotBlank(message = "genre cannot be empty")
    private String genre;

    @Min(value = 0,message = "Rating should be min 0")
    @Max(value =10,message = "Maximum Rating is 10")
    private double rating;

    @NotNull(message = "price cannot be empty")
    @Min(value = 100,message = "price Should be > 100")
    @Max(value = 999,message = "")
    private BigDecimal price;
}
