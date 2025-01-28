package com.zizen.foodorder.persistence.entity.impl;

import com.zizen.foodorder.persistence.entity.Entity;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import java.util.UUID;

public class Category extends Entity implements Comparable<Category> {

    private String name;

    public Category(UUID id, String name) {
        super(id);
        this.name = validatedName(name);

        if (this.isValid()) {
            throw new EntityArgumentException(errors);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = validatedName(name);
    }

    private String validatedName(String name) {
        final String templateName = "назви категорії";

        if (name == null || name.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (name.length() < 3) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 3));
        }
        if (name.length() > 100) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 100));
        }
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
            "name='" + name + '\'' +
            ", id=" + id +
            '}';
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.name);
    }
}
