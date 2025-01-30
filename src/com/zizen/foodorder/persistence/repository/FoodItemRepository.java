package com.zizen.foodorder.persistence.repository;

import com.google.gson.reflect.TypeToken;
import com.zizen.foodorder.persistence.JsonPathFactory;
import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.exception.JsonFileIOException;
import com.zizen.foodorder.persistence.util.FileUtil;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FoodItemRepository {

    private final List<FoodItem> foodItems;
    private final Path filePath;

    public FoodItemRepository() {
        this.filePath = JsonPathFactory.FOOD_ITEMS.getPath();
        this.foodItems = loadFoodItems();
    }

    public void add(FoodItem entity) {
        foodItems.add(entity);
        saveFoodItems();
    }

    public void update(FoodItem entity) {
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getId().equals(entity.getId())) {
                foodItems.set(i, entity); // Замінюємо весь об'єкт
                saveFoodItems();
                return;
            }
        }
        System.out.println("Елемент з таким ID не знайдений.");
    }


    public void delete(FoodItem entity) {
        foodItems.removeIf(foodItem -> foodItem.getId().equals(entity.getId()));
        saveFoodItems();
    }

    public List<FoodItem> getAllObjects() {
        return new ArrayList<>(foodItems);
    }

    public FoodItem getById(UUID id) {
        return foodItems.stream()
            .filter(foodItem -> foodItem.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<FoodItem> find(String value) {
        return foodItems.stream()
            .filter(foodItem -> foodItem.getName().toLowerCase().contains(value.toLowerCase()))
            .collect(Collectors.toList());
    }

    private List<FoodItem> loadFoodItems() {
        try {
            Type listType = new TypeToken<List<FoodItem>>() {
            }.getType();
            return FileUtil.loadFromJson(filePath.toString(), listType);
        } catch (JsonFileIOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveFoodItems() {
        try {
            FileUtil.saveToJson(filePath.toString(), foodItems);
        } catch (JsonFileIOException e) {
            e.printStackTrace();
        }
    }
}
