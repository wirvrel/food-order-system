package com.zizen.foodorder.presentation.menu;

public enum InitialMenu {
    SIGN_IN("🔑 Увійти"),
    SIGN_UP("📝 Зареєструватися"),
    EXIT("🚪 Вийти");

    private final String name;

    InitialMenu(String name) {
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
