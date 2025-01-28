package com.zizen.foodorder.persistence;

import java.nio.file.Path;

public enum JsonPathFactory {
    USERS("users.json"),
    ORDERS("orders.json"),
    CATEGORIES("categories.json"),
    FOOD_ITEMS("food_items.json");
    private static final String DATA_DIRECTORY = "data";
    private final String fileName;

    JsonPathFactory(String fileName) {
        this.fileName = fileName;
    }

    public Path getPath() {
        return Path.of(DATA_DIRECTORY, this.fileName);
    }
}
