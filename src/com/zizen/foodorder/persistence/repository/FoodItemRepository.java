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

public class FoodItemRepository implements GenericRepository<FoodItem> {

    private final List<FoodItem> foodItems;
    private final Path filePath;

    public FoodItemRepository() {
        this.filePath = JsonPathFactory.FOOD_ITEMS.getPath();
        this.foodItems = loadFoodItems();
    }

    @Override
    public void add(FoodItem entity) {
        foodItems.add(entity);
        saveFoodItems();
    }

    @Override
    public void update(FoodItem entity) {
        FoodItem existingFoodItem = getById(entity.getId());

        if (existingFoodItem != null) {
            // Оновлення полів FoodItem
            existingFoodItem.setName(entity.getName());
            existingFoodItem.setDescription(entity.getDescription());
            existingFoodItem.setPrice(entity.getPrice());
            existingFoodItem.setCategory(entity.getCategory());

            saveFoodItems();
        } else {
            System.out.println("Елемент з таким ID не знайдено.");
        }
    }

    @Override
    public void delete(FoodItem entity) {
        foodItems.removeIf(foodItem -> foodItem.getId().equals(entity.getId()));
        saveFoodItems();
    }

    @Override
    public List<FoodItem> getAllObjects() {
        return new ArrayList<>(foodItems);
    }

    @Override
    public FoodItem getById(UUID id) {
        return foodItems.stream()
            .filter(foodItem -> foodItem.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
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
