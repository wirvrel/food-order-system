package com.zizen.foodorder.presentation.menu;

import com.zizen.foodorder.persistence.entity.enums.Role;
import java.util.ArrayList;
import java.util.List;

public enum ViewMenu {
    VIEW_DISH("Переглянути меню"),
    VIEW_CATEGORIES("Переглянути категорії"),
    VIEW_ORDERS("Переглянути свої замовлення"),
    EXIT("Повернутися до головного меню");

    private final String name;

    ViewMenu(String name) {
        this.name = name;
    }

    public static List<ViewMenu> getAvailableOptions(Role role) {
        List<ViewMenu> options = new ArrayList<>();
        switch (role) {
            case ADMIN -> {
                options.add(VIEW_DISH);
                options.add(VIEW_CATEGORIES);
                options.add(VIEW_ORDERS);
                options.add(EXIT);
            }
            case USER -> {
                options.add(VIEW_DISH);
                options.add(VIEW_ORDERS);
                options.add(EXIT);
            }
        }
        return options;
    }

    public String getName() {
        return name;
    }
}
