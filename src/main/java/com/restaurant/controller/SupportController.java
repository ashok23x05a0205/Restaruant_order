package com.restaurant.controller;

import com.restaurant.entity.SupportTicket;
import com.restaurant.entity.User;
import com.restaurant.service.SupportTicketService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/support")
public class SupportController {
    
    @Autowired
    private SupportTicketService supportTicketService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listTickets(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            if (user.getRole() == User.Role.SUPPORT || user.getRole() == User.Role.ADMIN) {
                model.addAttribute("tickets", supportTicketService.findAll());
                return "support-tickets";
            } else {
                model.addAttribute("tickets", supportTicketService.findByUser(user));
                return "customer-tickets";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("/new")
    public String newTicket(Model model) {
        model.addAttribute("ticket", new SupportTicket());
        return "new-ticket";
    }
    
    @PostMapping("/new")
    public String createTicket(@Valid @ModelAttribute SupportTicket ticket, 
                              BindingResult result, Authentication auth) {
        if (result.hasErrors()) {
            return "new-ticket";
        }
        
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            ticket.setUser(user);
            supportTicketService.save(ticket);
            return "redirect:/support?success";
        }
        return "redirect:/login";
    }
    
    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Optional<SupportTicket> ticketOpt = supportTicketService.findById(id);
        
        if (ticketOpt.isPresent() && user != null) {
            SupportTicket ticket = ticketOpt.get();
            if (ticket.getUser().getId().equals(user.getId()) || 
                user.getRole() == User.Role.SUPPORT || 
                user.getRole() == User.Role.ADMIN) {
                model.addAttribute("ticket", ticket);
                return "ticket-detail";
            }
        }
        return "redirect:/support";
    }
    
    @PostMapping("/{id}/respond")
    public String respondToTicket(@PathVariable Long id, 
                                 @RequestParam String response, 
                                 Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null && (user.getRole() == User.Role.SUPPORT || user.getRole() == User.Role.ADMIN)) {
            supportTicketService.respondToTicket(id, response);
        }
        return "redirect:/support/" + id;
    }
}