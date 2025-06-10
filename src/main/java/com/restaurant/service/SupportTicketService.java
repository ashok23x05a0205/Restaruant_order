package com.restaurant.service;

import com.restaurant.entity.SupportTicket;
import com.restaurant.entity.User;
import com.restaurant.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
    public SupportTicket save(SupportTicket ticket) {
        return supportTicketRepository.save(ticket);
    }
    
    public List<SupportTicket> findAll() {
        return supportTicketRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<SupportTicket> findByUser(User user) {
        return supportTicketRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public Optional<SupportTicket> findById(Long id) {
        return supportTicketRepository.findById(id);
    }
    
    public List<SupportTicket> findByStatus(SupportTicket.TicketStatus status) {
        return supportTicketRepository.findByStatus(status);
    }
    
    public SupportTicket respondToTicket(Long ticketId, String response) {
        Optional<SupportTicket> ticketOpt = findById(ticketId);
        if (ticketOpt.isPresent()) {
            SupportTicket ticket = ticketOpt.get();
            ticket.setResponse(response);
            ticket.setRespondedAt(LocalDateTime.now());
            ticket.setStatus(SupportTicket.TicketStatus.RESOLVED);
            return save(ticket);
        }
        return null;
    }
    
    public long countOpenTickets() {
        return supportTicketRepository.findByStatus(SupportTicket.TicketStatus.OPEN).size();
    }
}