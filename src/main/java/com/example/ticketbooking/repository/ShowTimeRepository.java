package com.example.ticketbooking.repository;

import com.example.ticketbooking.entity.ShowTimeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTimeEntity, UUID>{
    Optional<ShowTimeEntity> findByShowTime(LocalDateTime showTime);
}
