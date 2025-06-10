package com.restaurant.controller;

import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.FoodItem;
import com.restaurant.entity.User;
import com.restaurant.service.OrderService;
import com.restaurant.service.FoodItemService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private FoodItemService foodItemService;
    
    @Autowired
    private UserService userService;
    @GetMapping("/orders")
    public String viewOrders(Model model, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            List<Order> orders = orderService.findByUser(user);
            model.addAttribute("orders", orders);
            return "customer-orders";
        }
        return "redirect:/login";
    }

    @GetMapping
    public String listOrders(Authentication auth, Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null) {
            if (user.getRole() == User.Role.ADMIN) {
                model.addAttribute("orders", orderService.findAll());
                return "admin-orders";
            } else {
                model.addAttribute("orders", orderService.findByUser(user));
                return "customer-orders";
            }
        }
        return "redirect:/login";
    }
    
    @GetMapping("/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("foodItems", foodItemService.findAvailable());
        return "new-order";
    }
    
    @PostMapping("/add-item")
    public String addItemToOrder(@RequestParam Long foodItemId, 
                                @RequestParam int quantity,
                                Authentication auth,
                                Model model) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Optional<FoodItem> foodItemOpt = foodItemService.findById(foodItemId);
        
        if (user != null && foodItemOpt.isPresent()) {
            Order order = new Order();
            order.setUser(user);
            
            OrderItem orderItem = new OrderItem(order, foodItemOpt.get(), quantity);
            order.getOrderItems().add(orderItem);
            
            orderService.save(order);
            return "redirect:/orders?success";
        }
        return "redirect:/menu";
    }
    
    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, 
                                   @RequestParam String status, 
                                   Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        if (user != null && user.getRole() == User.Role.ADMIN) {
            Optional<Order> orderOpt = orderService.findById(id);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setStatus(Order.OrderStatus.valueOf(status));
                orderService.save(order);
            }
        }
        return "redirect:/orders";
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, Authentication auth) {
        User user = userService.findByUsername(auth.getName()).orElse(null);
        Optional<Order> orderOpt = orderService.findById(id);
        
        if (orderOpt.isPresent() && user != null) {
            Order order = orderOpt.get();
            if (order.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ADMIN) {
                order.setStatus(Order.OrderStatus.CANCELLED);
                orderService.save(order);
            }
        }
        return "redirect:/orders";
    }
}