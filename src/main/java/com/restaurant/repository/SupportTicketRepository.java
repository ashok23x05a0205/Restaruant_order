package com.restaurant.repository;

import com.restaurant.entity.SupportTicket;
import com.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByUser(User user);
    List<SupportTicket> findByStatus(SupportTicket.TicketStatus status);
    List<SupportTicket> findByPriority(SupportTicket.Priority priority);
    List<SupportTicket> findAllByOrderByCreatedAtDesc();
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
}