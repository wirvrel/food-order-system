package com.zizen.foodorder.service;

import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import java.util.List;
import java.util.UUID;

public interface FoodItemService {

    // Додає новий продукт
    void addFoodItem(FoodItem foodItem);

    // Оновлює існуючий продукт
    void updateFoodItem(FoodItem foodItem);

    // Видаляє продукт за ID
    void deleteFoodItem(UUID id);

    // Отримує всі продукти
    List<FoodItem> getAllFoodItems();

    // Пошук продуктів за ключовим словом
    List<FoodItem> searchFoodItems(String keyword);

    // Отримує продукт за його ID
    FoodItem getFoodItemById(UUID id);
}
