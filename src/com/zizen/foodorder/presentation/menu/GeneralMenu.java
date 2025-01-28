package com.zizen.foodorder.presentation.menu;

import com.zizen.foodorder.persistence.entity.enums.Role;
import java.util.ArrayList;
import java.util.List;

public enum GeneralMenu {
    VIEW("Перегляд даних"),
    ADD("Додавання даних"),
    EDIT("Редагування даних"),
    DELETE("Видалення даних"),
    SEARCH("Пошук"),
    EXIT("Вихід");

    private final String name;

    GeneralMenu(String name) {
        this.name = name;
    }

    /**
     * Отримати доступні опції меню відповідно до ролі.
     *
     * @param role Роль користувача.
     * @return Список доступних пунктів меню.
     */
    public static List<GeneralMenu> getAvailableOptions(Role role) {
        List<GeneralMenu> options = new ArrayList<>();
        switch (role) {
            case ADMIN -> {
                options.add(VIEW);
                options.add(ADD);
                options.add(EDIT);
                options.add(DELETE);
                options.add(SEARCH);
                options.add(EXIT);
            }
            case USER -> {
                options.add(VIEW);
                options.add(ADD);
                options.add(SEARCH);
                options.add(EXIT);
            }
        }
        return options;
    }

    public String getName() {
        return name;
    }
}
