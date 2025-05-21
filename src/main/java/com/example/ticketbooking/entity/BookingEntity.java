package com.example.ticketbooking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String Email;

    @ManyToOne
    private MovieEntity movieEntity;

    @ManyToOne
    private TheaterEntity theaterEntity;

    @ManyToOne
    private ShowTimeEntity showTimeEntity;

    @ManyToOne
    @JoinColumn(name = "movie_show_details_entity_id")
    private MovieShowDetailsEntity movieShowDetailsEntity;

    private Integer seatCount;

    private BigDecimal totalAmount;

    private boolean isPaid;
}
