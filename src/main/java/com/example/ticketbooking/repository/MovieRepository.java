package com.example.ticketbooking.repository;

import com.example.ticketbooking.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, UUID> {
    @Query ("SELECT m FROM MovieEntity m WHERE m.movieName = ?1")
    Optional<MovieEntity> findMovieByName(String name);
}
