package com.zizen.foodorder.persistence.repository;

import com.google.gson.reflect.TypeToken;
import com.zizen.foodorder.persistence.JsonPathFactory;
import com.zizen.foodorder.persistence.entity.impl.Category;
import com.zizen.foodorder.persistence.exception.JsonFileIOException;
import com.zizen.foodorder.persistence.util.FileUtil;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CategoryRepository {

    private final List<Category> categories;
    private final Path filePath;

    public CategoryRepository() {
        this.filePath = JsonPathFactory.CATEGORIES.getPath();
        this.categories = loadCategories();
    }

    public void add(Category entity) {
        categories.add(entity);
        saveCategories();
    }

    public void update(Category entity) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(entity.getId())) {
                categories.set(i, entity); // Замінюємо весь об'єкт
                saveCategories();
                return;
            }
        }
        System.out.println("Категорія з таким ID не знайдена.");
    }

    public void delete(Category entity) {
        categories.removeIf(category -> category.getId().equals(entity.getId()));
        saveCategories();
    }

    public List<Category> getAllObjects() {
        return new ArrayList<>(categories);
    }

    public Category getById(UUID id) {
        return categories.stream()
            .filter(category -> category.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Category> find(String value) {
        return categories.stream()
            .filter(category -> category.getName().toLowerCase().contains(value.toLowerCase()))
            .collect(Collectors.toList());
    }

    private List<Category> loadCategories() {
        try {
            Type listType = new TypeToken<List<Category>>() {
            }.getType();
            return FileUtil.loadFromJson(filePath.toString(), listType);
        } catch (JsonFileIOException e) {
            // Логування помилки, якщо потрібно
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Зберігаємо категорії в файл
    private void saveCategories() {
        try {
            FileUtil.saveToJson(filePath.toString(), categories);
        } catch (JsonFileIOException e) {
            // Логування помилки, якщо потрібно
            e.printStackTrace();
        }
    }
}