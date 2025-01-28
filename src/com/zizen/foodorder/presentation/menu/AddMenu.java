package com.zizen.foodorder.presentation.menu;

import com.zizen.foodorder.persistence.entity.enums.Role;
import java.util.ArrayList;
import java.util.List;

public enum AddMenu {
    ADD_DISH("Додати страву"),
    ADD_CATEGORY("Додати категорію"),
    ADD_ORDER("Оформити замовлення"),
    EXIT("Повернутися до головного меню");

    private final String name;

    AddMenu(String name) {
        this.name = name;
    }

    /**
     * Отримати доступні опції додавання залежно від ролі.
     *
     * @param role Роль користувача.
     * @return Список доступних пунктів меню.
     */
    public static List<AddMenu> getAvailableOptions(Role role) {
        List<AddMenu> options = new ArrayList<>();
        switch (role) {
            case ADMIN -> {
                options.add(ADD_DISH);
                options.add(ADD_CATEGORY);
                options.add(ADD_ORDER);
                options.add(EXIT);
            }
            case USER -> {
                options.add(ADD_ORDER);
                options.add(EXIT);
            }
        }
        return options;
    }

    public String getName() {
        return name;
    }
}
