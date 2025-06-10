package com.restaurant.controller;

import com.restaurant.entity.FoodItem;
import com.restaurant.service.FoodItemService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/menu")
public class FoodController {
    
    @Autowired
    private FoodItemService foodItemService;
    @GetMapping("/menu")
    public String showMenu(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String category,
            Model model) {

        List<FoodItem> foodItems = foodItemService.searchAndFilterItems(search, category);

        model.addAttribute("foodItems", foodItems);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchKeyword", search);

        return "menu";
    }

    @GetMapping
    public String viewMenu(Model model, @RequestParam(required = false) String category) {
        if (category != null && !category.isEmpty()) {
            try {
                FoodItem.Category cat = FoodItem.Category.valueOf(category.toUpperCase());
                model.addAttribute("foodItems", foodItemService.findByCategory(cat));
                model.addAttribute("selectedCategory", category);
            } catch (IllegalArgumentException e) {
                model.addAttribute("foodItems", foodItemService.findAvailable());
            }
        } else {
            model.addAttribute("foodItems", foodItemService.findAvailable());
        }
        
        model.addAttribute("categories", FoodItem.Category.values());
        return "menu";
    }
    
    @GetMapping("/{id}")
    public String viewFoodItem(@PathVariable Long id, Model model) {
        return foodItemService.findById(id)
            .map(foodItem -> {
                model.addAttribute("foodItem", foodItem);
                return "food-detail";
            })
            .orElse("redirect:/menu");
    }
}