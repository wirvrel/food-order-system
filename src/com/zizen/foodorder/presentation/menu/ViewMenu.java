package com.zizen.foodorder.presentation.menu;

public enum ViewMenu {
    VIEW_DISH("Переглянути меню"),
    VIEW_CATEGORIES("Переглянути категорії"),
    VIEW_ORDERS("Переглянути замовлення"),
    EXIT("Повернутися до головного меню");

    private final String name;

    ViewMenu(String name) {
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