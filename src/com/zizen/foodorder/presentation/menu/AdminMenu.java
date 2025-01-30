package com.zizen.foodorder.presentation.menu;

public enum AdminMenu {
    VIEW("👀 Перегляд даних"),
    ADD("➕ Додавання даних"),
    EDIT("✏️ Редагування даних"),
    DELETE("❌ Видалення даних"),
    SEARCH("🔍 Пошук"),
    EXPORT_MENU("📤 Експортувати меню"),
    EXIT("🚪 Вихід");

    private final String name;

    AdminMenu(String name) {
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
