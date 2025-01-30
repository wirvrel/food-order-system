package com.zizen.foodorder.presentation.menu;

public enum AddMenu {
    ADD_DISH("🍽️ Додати страву"),
    ADD_CATEGORY("📂 Додати категорію"),
    ADD_ORDER("🛒 Оформити замовлення"),
    EXIT("🏠 Повернутися до головного меню");

    private final String name;

    AddMenu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
