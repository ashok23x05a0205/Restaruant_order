package com.restaurant.config;

import com.restaurant.entity.User;
import com.restaurant.entity.FoodItem;
import com.restaurant.service.UserService;
import com.restaurant.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FoodItemService foodItemService;
    
    @Override
    public void run(String... args) throws Exception {
        if (!userService.existsByUsername("admin")) {
            User admin = new User("admin", "admin@restaurant.com", "admin123", 
                                "System Administrator", "555-0001", User.Role.ADMIN);
            userService.registerUser(admin);
        }
        
        if (!userService.existsByUsername("support")) {
            User support = new User("support", "support@restaurant.com", "support123", 
                                  "Customer Support", "555-0002", User.Role.SUPPORT);
            userService.registerUser(support);
        }
        
        if (foodItemService.findAll().isEmpty()) {
            initializeFoodItems();
        }
    }
    
    private void initializeFoodItems() {
        foodItemService.save(new FoodItem("Caesar Salad", "Fresh romaine lettuce with parmesan cheese and croutons", 
            new BigDecimal("12.99"), FoodItem.Category.APPETIZER, "https://images.pexels.com/photos/1059905/pexels-photo-1059905.jpeg"));
        
        foodItemService.save(new FoodItem("Grilled Chicken", "Tender grilled chicken breast with herbs and spices", 
            new BigDecimal("18.99"), FoodItem.Category.MAIN_COURSE, "https://images.pexels.com/photos/106343/pexels-photo-106343.jpeg"));
        
        foodItemService.save(new FoodItem("Beef Steak", "Premium beef steak cooked to perfection", 
            new BigDecimal("24.99"), FoodItem.Category.MAIN_COURSE, "https://images.pexels.com/photos/361184/asparagus-steak-veal-steak-veal-361184.jpeg"));
        
        foodItemService.save(new FoodItem("Chocolate Cake", "Rich chocolate cake with cream frosting", 
            new BigDecimal("8.99"), FoodItem.Category.DESSERT, "https://images.pexels.com/photos/291528/pexels-photo-291528.jpeg"));
        
        foodItemService.save(new FoodItem("Fresh Orange Juice", "Freshly squeezed orange juice", 
            new BigDecimal("4.99"), FoodItem.Category.BEVERAGE, "https://images.pexels.com/photos/96974/pexels-photo-96974.jpeg"));
        
        foodItemService.save(new FoodItem("Pasta Carbonara", "Classic Italian pasta with cream sauce", 
            new BigDecimal("16.99"), FoodItem.Category.MAIN_COURSE, "https://images.pexels.com/photos/1279330/pexels-photo-1279330.jpeg"));
        
        foodItemService.save(new FoodItem("Garlic Bread", "Toasted bread with garlic butter", 
            new BigDecimal("6.99"), FoodItem.Category.APPETIZER, "https://images.pexels.com/photos/209206/pexels-photo-209206.jpeg"));
        
        foodItemService.save(new FoodItem("Tiramisu", "Traditional Italian dessert", 
            new BigDecimal("9.99"), FoodItem.Category.DESSERT, "https://images.pexels.com/photos/6880219/pexels-photo-6880219.jpeg"));
    }
}