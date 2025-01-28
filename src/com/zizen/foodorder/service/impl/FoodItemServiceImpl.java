package com.zizen.foodorder.service.impl;

import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.repository.FoodItemRepository;
import com.zizen.foodorder.service.FoodItemService;
import java.util.List;
import java.util.UUID;

public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public FoodItemServiceImpl() {
        this.foodItemRepository = new FoodItemRepository(); // Ініціалізація репозиторію
    }

    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItemRepository.add(foodItem); // Додаємо продукт до репозиторію
    }

    @Override
    public void updateFoodItem(FoodItem foodItem) {
        foodItemRepository.update(foodItem); // Оновлюємо продукт у репозиторії
    }

    @Override
    public void deleteFoodItem(UUID id) {
        FoodItem foodItem = foodItemRepository.getById(id);
        if (foodItem != null) {
            foodItemRepository.delete(foodItem);
        }
    }

    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.getAllObjects(); // Повертаємо всі продукти
    }

    @Override
    public List<FoodItem> searchFoodItems(String keyword) {
        return foodItemRepository.find(keyword); // Повертаємо продукти, що містять ключове слово
    }

    @Override
    public FoodItem getFoodItemById(UUID id) {
        return foodItemRepository.getById(id); // Повертаємо продукт за ID
    }
}
