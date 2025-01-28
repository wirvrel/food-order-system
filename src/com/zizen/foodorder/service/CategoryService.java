package com.zizen.foodorder.service;

import com.zizen.foodorder.persistence.entity.impl.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

    // Додає нову категорію
    void addCategory(Category category);

    // Оновлює існуючу категорію
    void updateCategory(Category category);

    // Видаляє категорію за ID
    void deleteCategory(UUID id);

    // Отримує всі категорії
    List<Category> getAllCategories();

    // Пошук категорій за ключовим словом
    List<Category> searchCategories(String keyword);

    // Отримує категорію за її ID
    Category getCategoryById(UUID id);
}
