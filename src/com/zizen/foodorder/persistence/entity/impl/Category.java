package com.zizen.foodorder.persistence.entity.impl;

import com.zizen.foodorder.persistence.entity.Entity;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import java.util.UUID;

public class Category extends Entity implements Comparable<Category> {

    private String name;

    public Category(UUID id, String name) {
        super(id);
        setName(name); // Використовуємо сеттер для перевірки одразу при створенні
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        errors.clear(); // Очищаємо попередні помилки

        if (name == null || name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("назви категорії"));
        }
        if (name != null && name.length() < 3) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted("назви категорії", 3));
        }
        if (name != null && name.length() > 100) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted("назви категорії", 100));
        }

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors); // Викидаємо виняток, якщо є помилки
        }

        this.name = name; // Якщо помилок немає – оновлюємо значення
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "Category{" +
            "name='" + name + '\'' +
            ", id=" + id +
            '}';
    }
}
