package com.restaurant.controller;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.User;
import com.restaurant.service.ReservationService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @GetMapping("/new")
    public String showReservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "new-reservation"; // Template for the form
    }

    @PostMapping("/new")
    public String createReservation(@Valid @ModelAttribute Reservation reservation,
                                    BindingResult result,
                                    Authentication auth,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "new-reservation";
        }

        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            reservation.setUser(user);
            reservation.setCustomerName(user.getFullName());
            reservation.setCustomerEmail(user.getEmail());
            reservation.setCustomerPhone(user.getPhone());

            try {
                reservationService.save(reservation);
                redirectAttributes.addFlashAttribute("successMessage", "Reservation created successfully!");
                return "redirect:/reservations";
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Failed to create reservation. Please try again.");
                return "new-reservation";
            }
        }

        return "redirect:/login";
    }

    @GetMapping
    public String listReservations(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            if (user.getRole() == User.Role.ADMIN) {
                model.addAttribute("reservations", reservationService.findAll());
                return "admin-reservations";
            } else {
                model.addAttribute("reservations", reservationService.findByUser(user));
                return "customer-reservations";
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null && user.getRole() == User.Role.ADMIN) {
            reservationService.findById(id).ifPresent(reservation -> {
                reservation.setStatus(Reservation.ReservationStatus.valueOf(status));
                reservationService.save(reservation);
            });
        }
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/cancel")
    public String cancelReservation(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        reservationService.findById(id).ifPresent(reservation -> {
            if (user != null && (reservation.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ADMIN)) {
                reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
                reservationService.save(reservation);
            }
        });
        return "redirect:/reservations";
    }
}
