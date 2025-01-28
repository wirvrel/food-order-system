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

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        final String templateName = "назви страви";

        if (name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (name.length() > 50) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 50));
        }

        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        final String templateName = "опису страви";

        if (description.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (description.length() > 200) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 200));
        }

        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        final String templateName = "ціни";

        if (price <= 0) {
            errors.add("Поле %s має бути більше нуля.".formatted(templateName));
        }

        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("категорії"));
        }

        this.category = category;
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

    @Override
    public int compareTo(FoodItem o) {
        return this.name.compareTo(o.name);
    }
}
