package com.restaurant.service;

import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.User;
import com.restaurant.repository.OrderRepository;
import com.restaurant.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    public List<Order> findAll() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }
    
    public List<Order> findByUser(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Transactional
    public Order save(Order order) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            total = total.add(item.getSubtotal());
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }
    
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
    
    public List<Order> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public long countPendingOrders() {
        return orderRepository.findByStatus(Order.OrderStatus.PENDING).size();
    }
}