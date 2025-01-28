package com.zizen.foodorder.presentation.menu;

public enum DeleteMenu {
    DELETE_DISH("Видалити страву"),
    DELETE_CATEGORY("Видалити категорію"),
    CANCEL_BOOKING("Скасувати бронювання"),
    EXIT("Повернутися до головного меню");

    private final String name;

    DeleteMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
