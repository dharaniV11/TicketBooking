package com.example.ticketbooking.repository;

import com.example.ticketbooking.entity.MovieEntity;
import com.example.ticketbooking.entity.MovieShowDetailsEntity;
import com.example.ticketbooking.entity.ShowTimeEntity;
import com.example.ticketbooking.entity.TheaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MovieShowDetailsRepository extends JpaRepository<MovieShowDetailsEntity, UUID> {
    Optional<MovieShowDetailsEntity> findByMovieAndTheaterAndShowTime(MovieEntity movie, TheaterEntity theater, ShowTimeEntity showTime);

    boolean existsByTheaterIdAndShowTimeId(UUID theaterId, UUID showTimeId);
}