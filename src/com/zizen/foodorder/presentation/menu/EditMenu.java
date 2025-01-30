package com.zizen.foodorder.presentation.menu;

public enum EditMenu {
    EDIT_DISH("🍽️ Редагувати страву"),
    EDIT_CATEGORY("📂 Редагувати категорію"),
    EXIT("🏠 Повернутися до головного меню");

    private final String name;

    EditMenu(String name) {
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
