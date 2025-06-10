package com.restaurant.service;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.User;
import com.restaurant.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
    
    public List<Reservation> findAll() {
        return reservationRepository.findAllByOrderByReservationDateDesc();
    }
    
    public List<Reservation> findByUser(User user) {
        return reservationRepository.findByUserOrderByReservationDateDesc(user);
    }
    
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    
    public List<Reservation> findByStatus(Reservation.ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
    
    public List<Reservation> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return reservationRepository.findByReservationDateBetween(start, end);
    }
    
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    
    public long countPendingReservations() {
        return reservationRepository.findByStatus(Reservation.ReservationStatus.PENDING).size();
    }
    
    public long countTodayReservations() {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        return reservationRepository.findByReservationDateBetween(startOfDay, endOfDay).size();
    }

	public void saveReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}
}