package com.restaurant.repository;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByUserOrderByReservationDateDesc(User user);
    List<Reservation> findAllByOrderByReservationDateDesc();
}