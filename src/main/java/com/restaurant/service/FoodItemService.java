package com.restaurant.service;

import com.restaurant.entity.FoodItem;
import com.restaurant.repository.FoodItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FoodItemService {
    
    @Autowired
    private FoodItemRepository foodItemRepository;
    
    public List<FoodItem> findAll() {
        return foodItemRepository.findAll();
    }
    
    public List<FoodItem> findAvailable() {
        return foodItemRepository.findByAvailableTrue();
    }
    
    public List<FoodItem> findByCategory(FoodItem.Category category) {
        return foodItemRepository.findByCategoryAndAvailableTrue(category);
    }
    
    public Optional<FoodItem> findById(Long id) {
        return foodItemRepository.findById(id);
    }
    
    public FoodItem save(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }
    
    public void deleteById(Long id) {
        foodItemRepository.deleteById(id);
    }
    
    public List<FoodItem> getAllCategories(String name) {
        return foodItemRepository.findByNameContainingIgnoreCase(name);
    }

	public List<FoodItem> searchAndFilterItems(String search, String category) {
		// TODO Auto-generated method stub
		return null;
	}


	
}