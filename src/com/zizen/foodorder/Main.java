package com.zizen.foodorder;

import com.zizen.foodorder.presentation.handler.MainMenuHandler;

public class Main {

    public static void main(String[] args) {
        MainMenuHandler mainMenuHandler = new MainMenuHandler();
        mainMenuHandler.showMenu();
    }
}
