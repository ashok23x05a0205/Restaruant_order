package com.restaurant.controller;

import com.restaurant.entity.User;
import com.restaurant.service.ReservationService;
import com.restaurant.service.SupportTicketService;
import com.restaurant.service.OrderService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private SupportTicketService supportTicketService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByUsername(auth.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("user", user);
                
                if (user.getRole() == User.Role.ADMIN) {
                    model.addAttribute("totalReservations", reservationService.findAll().size());
                    model.addAttribute("pendingReservations", reservationService.countPendingReservations());
                    model.addAttribute("todayReservations", reservationService.countTodayReservations());
                    model.addAttribute("openTickets", supportTicketService.countOpenTickets());
                    model.addAttribute("pendingOrders", orderService.countPendingOrders());
                    return "admin-dashboard";
                } else if (user.getRole() == User.Role.SUPPORT) {
                    model.addAttribute("openTickets", supportTicketService.countOpenTickets());
                    model.addAttribute("totalTickets", supportTicketService.findAll().size());
                    return "support-dashboard";
                } else {
                    model.addAttribute("userReservations", reservationService.findByUser(user));
                    model.addAttribute("userOrders", orderService.findByUser(user));
                    return "customer-dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}