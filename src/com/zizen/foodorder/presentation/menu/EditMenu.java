package com.zizen.foodorder.presentation.menu;

public enum EditMenu {
    EDIT_DISH("Редагувати страву"),
    EDIT_CATEGORY("Редагувати категорію"),
    EDIT_ORDER("Редагувати замовлення"),
    EXIT("Повернутися до головного меню");

    private final String name;

    EditMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
