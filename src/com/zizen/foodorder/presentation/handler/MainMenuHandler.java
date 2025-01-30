package com.zizen.foodorder.presentation.handler;

import com.zizen.foodorder.persistence.entity.enums.Role;
import com.zizen.foodorder.persistence.entity.impl.User;
import com.zizen.foodorder.presentation.menu.AddMenu;
import com.zizen.foodorder.presentation.menu.AdminMenu;
import com.zizen.foodorder.presentation.menu.DeleteMenu;
import com.zizen.foodorder.presentation.menu.EditMenu;
import com.zizen.foodorder.presentation.menu.InitialMenu;
import com.zizen.foodorder.presentation.menu.SearchMenu;
import com.zizen.foodorder.presentation.menu.UserMenu;
import com.zizen.foodorder.presentation.menu.ViewMenu;
import com.zizen.foodorder.service.CategoryService;
import com.zizen.foodorder.service.FoodItemService;
import com.zizen.foodorder.service.OrderService;
import com.zizen.foodorder.service.impl.AuthServiceImpl;
import com.zizen.foodorder.service.impl.CategoryServiceImpl;
import com.zizen.foodorder.service.impl.FoodItemServiceImpl;
import com.zizen.foodorder.service.impl.OrderServiceImpl;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;

public class MainMenuHandler {

    private final AuthServiceImpl authRepository;
    private final FoodItemMenuHandler foodItemMenuHandler;
    private final AuthMenuHandler authMenuHandler;
    private final CategoryMenuHandler categoryMenuHandler;
    private final OrderMenuHandler orderMenuHandler;
    private final Scanner scanner;

    public MainMenuHandler() {
        this.authRepository = AuthServiceImpl.getInstance();
        CategoryService categoryService = new CategoryServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        FoodItemService foodItemService = new FoodItemServiceImpl();
        this.foodItemMenuHandler = new FoodItemMenuHandler(foodItemService, categoryService);
        this.orderMenuHandler = new OrderMenuHandler(orderService, foodItemService,
            categoryService);
        this.categoryMenuHandler = new CategoryMenuHandler(categoryService);
        this.authMenuHandler = new AuthMenuHandler(authRepository);
        this.scanner = new Scanner(System.in);
    }

    public void showMenu() {
        if (!authRepository.isAuthenticated()) {
            showInitialMenu();
        } else {
            User currentUser = authRepository.getUser();
            showRoleBasedMenu(currentUser.getRole());
        }
    }

    private void showInitialMenu() {
        clearConsole();
        printMenuHeader("✨ Система замовлення їжі ✨");
        printMenuOptions(InitialMenu.values());
        handleMenuChoice(InitialMenu.values(), this::handleInitialMenuChoice);
    }

    private void handleInitialMenuChoice(int choice) {
        switch (InitialMenu.values()[choice - 1]) {
            case SIGN_IN -> signIn();
            case SIGN_UP -> signUp();
            case EXIT -> {
                System.out.println(
                    Ansi.ansi().fg(Ansi.Color.RED).a("Вихід із програми.До зустрічі!"));
                System.exit(0);  // Завершує програму
            }
        }
    }

    private void signIn() {
        clearConsole();
        if (authMenuHandler.signIn()) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("Авторизація успішна!").reset());
            User currentUser = authRepository.getUser();
            showRoleBasedMenu(currentUser.getRole());
        }
    }

    private void signUp() {
        clearConsole();
        boolean isRegistered = authMenuHandler.registerUser();
        if (isRegistered) {
            User currentUser = authRepository.getUser();
            if (currentUser != null) {
                showRoleBasedMenu(currentUser.getRole());
            } else {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED)
                    .a("Помилка: Не вдалося отримати дані користувача.").reset());
                showInitialMenu();
            }
        } else {
            System.out.println(
                Ansi.ansi().fg(Ansi.Color.RED).a("Реєстрація не вдалася. Спробуйте ще раз.")
                    .reset());
            showInitialMenu();
        }
    }

    private void showRoleBasedMenu(Role role) {
        clearConsole();
        switch (role) {
            case ADMIN -> showAdminMenu();
            case USER -> showUserMenu();
            default -> throw new IllegalArgumentException("Невідома роль: " + role);
        }
    }

    private void showAdminMenu() {
        clearConsole();
        printMenuHeader("\uD83D\uDD27 Адміністративне меню");
        printMenuOptions(AdminMenu.values());
        handleMenuChoice(AdminMenu.values(), this::handleAdminMenuChoice);
    }

    private void handleAdminMenuChoice(int choice) {
        switch (AdminMenu.values()[choice - 1]) {
            case VIEW -> showViewMenu();
            case ADD -> showAddMenu();
            case EDIT -> showEditMenu();
            case DELETE -> showDeleteMenu();
            case SEARCH -> showSearchMenu();
            case EXPORT_MENU -> foodItemMenuHandler.exportMenuToCSV();
            case EXIT -> exit();
        }
    }

    private void showUserMenu() {
        clearConsole();
        printMenuHeader("\uD83D\uDC64 Меню користувача");
        printMenuOptions(UserMenu.values());
        handleMenuChoice(UserMenu.values(), this::handleUserMenuChoice);
    }

    private void handleUserMenuChoice(int choice) {
        switch (UserMenu.values()[choice - 1]) {
            case VIEW_MENU -> foodItemMenuHandler.viewAllFoodItems();
            case PLACE_ORDER -> orderMenuHandler.createOrder();
            case VIEW_ORDERS -> orderMenuHandler.viewMyOrders();
            case CANCEL_ORDER -> orderMenuHandler.deleteOrder();
            case SEARCH_ITEM -> showSearchMenu();
            case EXPORT_MENU -> foodItemMenuHandler.exportMenuToCSV();
            case EXIT -> exit();
        }
        showMenu();
    }

    private void showSearchMenu() {
        clearConsole();
        printMenuHeader("\uD83D\uDD0D Меню пошуку");
        System.out.println(
            Ansi.ansi().fg(Ansi.Color.YELLOW).a("Введіть ключове слово для пошуку: ").reset());
        printMenuOptions(SearchMenu.values());
        handleMenuChoice(SearchMenu.values(), this::handleSearchMenuChoice);
    }

    private void handleSearchMenuChoice(int choice) {
        switch (SearchMenu.values()[choice - 1]) {
            case SEARCH_DISHES -> foodItemMenuHandler.searchFoodItems();
            case SEARCH_CATEGORIES -> categoryMenuHandler.searchCategories();
            case SEARCH_ORDERS -> orderMenuHandler.searchOrders();
            case RETURN_TO_MAIN -> showMenu();
        }
        showSearchMenu();
    }

    private void showAddMenu() {
        clearConsole();
        printMenuHeader("➕ Меню додавання");
        printMenuOptions(AddMenu.values());
        handleMenuChoice(AddMenu.values(), this::handleAddMenuChoice);
    }

    private void handleAddMenuChoice(int choice) {
        switch (AddMenu.values()[choice - 1]) {
            case ADD_DISH -> foodItemMenuHandler.createFoodItem();
            case ADD_CATEGORY -> categoryMenuHandler.createCategory();
            case ADD_ORDER -> orderMenuHandler.createOrder();
            case EXIT -> showMenu();
        }
        showAddMenu();
    }

    private void showViewMenu() {
        clearConsole();
        printMenuHeader("\uD83D\uDCCA Перегляд даних");
        printMenuOptions(ViewMenu.values());
        handleMenuChoice(ViewMenu.values(), this::handleViewMenuChoice);
    }

    private void handleViewMenuChoice(int choice) {
        switch (ViewMenu.values()[choice - 1]) {
            case VIEW_DISH -> foodItemMenuHandler.viewAllFoodItems();
            case VIEW_CATEGORIES -> categoryMenuHandler.viewAllCategories();
            case VIEW_ORDERS -> orderMenuHandler.viewAllOrders();
            case EXIT -> showMenu();
        }
        showViewMenu();
    }

    private void showEditMenu() {
        clearConsole();
        printMenuHeader("✏\uFE0F Меню редагування");
        printMenuOptions(EditMenu.values());
        handleMenuChoice(EditMenu.values(), this::handleEditMenuChoice);
    }

    private void handleEditMenuChoice(int choice) {
        switch (EditMenu.values()[choice - 1]) {
            case EDIT_DISH -> foodItemMenuHandler.updateFoodItem();
            case EDIT_CATEGORY -> categoryMenuHandler.updateCategory();
            case EXIT -> showMenu();
        }
        showEditMenu();
    }

    private void showDeleteMenu() {
        clearConsole();
        printMenuHeader("\uD83D\uDDD1 Меню видалення");
        printMenuOptions(DeleteMenu.values());
        handleMenuChoice(DeleteMenu.values(), this::handleDeleteMenuChoice);
    }

    private void handleDeleteMenuChoice(int choice) {
        switch (DeleteMenu.values()[choice - 1]) {
            case DELETE_DISH -> foodItemMenuHandler.deleteFoodItem();
            case DELETE_CATEGORY -> categoryMenuHandler.deleteCategory();
            case CANCEL_ORDER -> orderMenuHandler.deleteOrder();
            case EXIT -> showMenu();
        }
        showDeleteMenu();
    }

    private void handleMenuChoice(Enum<?>[] menuItems, MenuChoiceHandler handler) {
        while (true) { // Додаємо цикл для повторного запиту, поки не буде правильного введення
            System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
            try {
                String input = scanner.nextLine();
                int choice = Integer.parseInt(
                    input); // Пробуємо перетворити введене значення в число

                // Перевірка, чи індекс знаходиться в межах допустимих значень
                if (choice < 1 || choice > menuItems.length) {
                    System.out.println(
                        Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.")
                            .reset());
                    continue; // Повертаємося до початку циклу для нового введення
                }

                handler.handle(choice); // Якщо індекс коректний, передаємо в обробник
                break; // Виходимо з циклу після успішного вибору
            } catch (NumberFormatException e) {
                // Якщо не вдалося перетворити введення в число
                System.out.println(
                    Ansi.ansi().fg(Ansi.Color.RED)
                        .a("Некоректний формат введення, спробуйте ще раз.")
                        .reset());
            } catch (Exception e) {
                // Загальна обробка інших виключень
                System.out.println(
                    Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.")
                        .reset());
            }
        }
    }


    private void printMenuHeader(String header) {
        System.out.println(
            Ansi.ansi().fg(Ansi.Color.YELLOW).a("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("   " + header).reset());
        System.out.println(
            Ansi.ansi().fg(Ansi.Color.YELLOW).a("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
    }

    private void printMenuOptions(Enum<?>[] menuItems) {
        for (int i = 0; i < menuItems.length; i++) {
            // Використовуємо getName() для виведення тексту
            System.out.printf(
                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
                menuItems[i].toString()); // Викликаємо toString(), який виведе назву через getName()
        }
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void exit() {
        authRepository.logout();
        showInitialMenu();
    }

}
