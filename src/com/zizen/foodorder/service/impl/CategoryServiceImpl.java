package com.zizen.foodorder.service.impl;

import com.zizen.foodorder.persistence.entity.impl.Category;
import com.zizen.foodorder.persistence.repository.CategoryRepository;
import com.zizen.foodorder.service.CategoryService;
import java.util.List;
import java.util.UUID;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl() {
        this.categoryRepository = new CategoryRepository(); // Ініціалізація репозиторію
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.add(category); // Додаємо категорію до репозиторію
    }

    @Override
    public void updateCategory(Category category) {
        categoryRepository.update(category); // Оновлюємо категорію в репозиторії
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.getById(id);
        if (category != null) {
            categoryRepository.delete(category);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllObjects(); // Повертаємо всі категорії
    }

    @Override
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.find(keyword); // Повертаємо категорії, що містять ключове слово
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.getById(id); // Повертаємо категорію за ID
    }
}
