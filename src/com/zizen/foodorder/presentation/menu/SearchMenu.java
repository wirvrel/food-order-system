package com.zizen.foodorder.presentation.menu;

public enum SearchMenu {
    SEARCH_DISHES("🍽️ Шукати страви"),
    SEARCH_CATEGORIES("📂 Шукати категорії"),
    SEARCH_ORDERS("🛒 Шукати замовлення"),
    RETURN_TO_MAIN("🏠 Повернутися до головного меню");

    private final String name;

    SearchMenu(String name) {
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
