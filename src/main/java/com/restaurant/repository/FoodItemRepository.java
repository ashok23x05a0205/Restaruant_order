package com.restaurant.repository;

import com.restaurant.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByCategory(FoodItem.Category category);
    List<FoodItem> findByAvailableTrue();
    List<FoodItem> findByCategoryAndAvailableTrue(FoodItem.Category category);
    List<FoodItem> findByNameContainingIgnoreCase(String name);
}