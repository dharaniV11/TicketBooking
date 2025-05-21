package com.example.ticketbooking.repository;

import com.example.ticketbooking.entity.TheaterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TheaterRepository extends JpaRepository<TheaterEntity, UUID> {
    @Query("SELECT t FROM TheaterEntity t WHERE t.theaterName = ?1")
    Optional<TheaterEntity> findByName(String name);
}
