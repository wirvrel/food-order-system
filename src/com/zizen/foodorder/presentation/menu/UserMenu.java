package com.zizen.foodorder.presentation.menu;

public enum UserMenu {
    VIEW_MENU("🍽️ Переглянути меню"),
    PLACE_ORDER("🛒 Оформити замовлення"),
    VIEW_ORDERS("📜 Переглянути мої замовлення"),
    CANCEL_ORDER("❌ Скасувати замовлення"),
    SEARCH_ITEM("🔍 Пошук"),
    EXPORT_MENU("📤 Експортувати меню"),
    EXIT("🚪 Вихід");

    private final String name;

    UserMenu(String name) {
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
