package com.zizen.foodorder.persistence.entity.impl;

import com.zizen.foodorder.persistence.entity.Entity;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import java.util.UUID;

public class FoodItem extends Entity implements Comparable<FoodItem> {

    private String name;
    private String description;
    private double price;
    private Category category;

    public FoodItem(UUID id, String name, String description, double price, Category category) {
        super(id);
        setName(name);
        setDescription(description);
        setPrice(price);
        setCategory(category);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        errors.clear(); // Очищаємо попередні помилки

        if (name == null || name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("назви страви"));
        }
        if (name != null && name.length() < 3) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted("назви страви", 3));
        }
        if (name != null && name.length() > 50) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted("назви страви", 50));
        }

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        errors.clear();

        if (description == null || description.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("опису страви"));
        }
        if (description != null && description.length() > 200) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted("опису страви", 200));
        }

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        errors.clear();

        if (price <= 0) {
            errors.add("Поле ціни має бути більше нуля.");
        }

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        errors.clear();

        if (category == null) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("категорії"));
        }

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }

        this.category = category;
    }

    @Override
    public int compareTo(FoodItem o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "FoodItem{" +
            "name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", category=" + category +
            ", id=" + id +
            '}';
    }
}
