//package com.zizen.foodorder.presentation.handler;
//
//import com.zizen.foodorder.persistence.entity.enums.Role;
//import com.zizen.foodorder.persistence.entity.impl.User;
//import com.zizen.foodorder.presentation.menu.AddMenu;
//import com.zizen.foodorder.presentation.menu.DeleteMenu;
//import com.zizen.foodorder.presentation.menu.EditMenu;
//import com.zizen.foodorder.presentation.menu.GeneralMenu;
//import com.zizen.foodorder.presentation.menu.InitialMenu;
//import com.zizen.foodorder.presentation.menu.SearchMenu;
//import com.zizen.foodorder.presentation.menu.ViewMenu;
//import com.zizen.foodorder.service.CategoryService;
//import com.zizen.foodorder.service.impl.AuthServiceImpl;
//import com.zizen.foodorder.service.impl.CategoryServiceImpl;
//import java.util.List;
//import java.util.Scanner;
//import org.fusesource.jansi.Ansi;
//
//public class MainMenuHandler {
//
//    private final AuthServiceImpl authRepository;
//    private final ArticleMenuHandler articleMenuHandler;
//    private final CategoryMenuHandler categoryMenuHandler;
//    private final FeedbackMenuHandler feedbackMenuHandler;
//    private final Scanner scanner;
//
//    public MainMenuHandler() {
//        this.authRepository = AuthServiceImpl.getInstance();
//        ArticleRepository articleRepository = new ArticleRepository();
//        ArticleService articleService = new ArticleServiceImpl(articleRepository);
//        CategoryService categoryService = new CategoryServiceImpl();
//        FeedbackService feedbackService = new FeedbackServiceImpl();
//        this.articleMenuHandler = new ArticleMenuHandler(articleService);
//        this.feedbackMenuHandler = new FeedbackMenuHandler(feedbackService);
//        this.categoryMenuHandler = new CategoryMenuHandler(categoryService);
//        this.scanner = new Scanner(System.in);
//    }
//
//    public void showMenu() {
//        if (!authRepository.isAuthenticated()) {
//            showInitialMenu();
//        } else {
//            User currentUser = authRepository.getUser();
//            showRoleBasedMenu(currentUser.getRole());
//        }
//    }
//
//    private void showInitialMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(
//            Ansi.ansi().fg(Ansi.Color.CYAN).a("   Інтерактивна енциклопедія для дітей").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        for (int i = 0; i < InitialMenu.values().length; i++) {
//            InitialMenu menu = InitialMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleInitialMenuChoice();
//    }
//
//    private void handleInitialMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            InitialMenu selectedMenu = InitialMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case SIGN_IN -> signIn();
//                case SIGN_UP -> signUp();
//                case VIEW_ARTICLES -> {
//                    articleMenuHandler.viewAllArticles();
//                    showInitialMenu();
//                }
//
//                case EXIT -> {
//                    System.out.println(
//                        Ansi.ansi().fg(Ansi.Color.RED).a("Вихід із програми. До побачення!")
//                            .reset());
//                    System.exit(0);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showInitialMenu();
//        }
//    }
//
//    private void signIn() {
//        clearConsole();
//        if (authRepository.signIn()) {
//            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("Авторизація успішна!").reset());
//            User currentUser = authRepository.getUser();
//            showRoleBasedMenu(currentUser.getRole());
//        }
//    }
//
//    private void signUp() {
//        clearConsole();
//        boolean isRegistered = authRepository.registerUser();
//        if (isRegistered) {
//            User currentUser = authRepository.getUser();
//            if (currentUser != null) {
//                showRoleBasedMenu(currentUser.getRole());
//            } else {
//                System.out.println(Ansi.ansi().fg(Ansi.Color.RED)
//                    .a("Помилка: Не вдалося отримати дані користувача.").reset());
//                showInitialMenu();
//            }
//        } else {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Реєстрація не вдалася. Спробуйте ще раз.")
//                    .reset());
//            showInitialMenu();
//        }
//    }
//
//    private void showRoleBasedMenu(Role role) {
//        clearConsole();
//        switch (role) {
//            case ADMIN -> showAdminMenu();
//            case USER -> showUserMenu();
//            default -> throw new IllegalArgumentException("Невідома роль: " + role);
//        }
//    }
//
//    private void showAdminMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Адміністративне меню").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        for (int i = 0; i < GeneralMenu.values().length; i++) {
//            GeneralMenu menu = GeneralMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleAdminMenuChoice();
//    }
//
//    private void handleAdminMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            GeneralMenu selectedMenu = GeneralMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case VIEW -> showViewMenu();
//                case ADD -> showAddMenu();
//                case EDIT -> showEditMenu();
//                case DELETE -> showDeleteMenu();
//                case SEARCH -> showSearchMenu();
//                case EXIT -> {
//                    authRepository.logout();
//                    showInitialMenu();
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showAdminMenu();
//        }
//    }
//
//    private void showUserMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Меню користувача").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//
//        // Отримуємо доступні опції для користувача
//        List<GeneralMenu> availableOptions = GeneralMenu.getAvailableOptions(Role.USER);
//
//        // Виводимо всі доступні опції для користувача
//        for (int i = 0; i < availableOptions.size(); i++) {
//            GeneralMenu menu = availableOptions.get(i);
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleUserMenuChoice(availableOptions);
//    }
//
//    private void handleUserMenuChoice(List<GeneralMenu> availableOptions) {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            // Зчитуємо вибір користувача
//            int choice = Integer.parseInt(scanner.nextLine());
//
//            // Перевіряємо, чи вибір є дійсним
//            if (choice < 1 || choice > availableOptions.size()) {
//                throw new IllegalArgumentException("Некоректний вибір");
//            }
//
//            GeneralMenu selectedMenu = availableOptions.get(choice - 1); // Вибір на основі індексу
//            switch (selectedMenu) {
//                case VIEW -> showViewMenu();
//                case ADD -> showAddMenu();
//                case SEARCH -> showSearchMenu();
//                case EXIT -> {
//                    authRepository.logout();
//                    showInitialMenu();
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showUserMenu(); // Повторно відобразити меню у випадку помилки
//        }
//    }
//
//    private void showSearchMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Меню пошуку").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(
//            Ansi.ansi().fg(Ansi.Color.YELLOW).a("Введіть ключове слово для пошуку: ").reset());
//
//        for (int i = 0; i < SearchMenu.values().length; i++) {
//            SearchMenu menu = SearchMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleSearchMenuChoice();
//    }
//
//    private void handleSearchMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            SearchMenu selectedMenu = SearchMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case SEARCH_ARTICLES -> {
//                    articleMenuHandler.searchArticles();
//                    showSearchMenu();
//                }
//                case SEARCH_CATEGORIES -> {
//                    categoryMenuHandler.searchCategories();
//                    showSearchMenu();
//                }
//                case SEARCH_FEEDBACKS -> {
//                    feedbackMenuHandler.searchFeedbacks();
//                    showSearchMenu();
//                }
//                case RETURN_TO_MAIN -> showMenu();
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showSearchMenu();
//        }
//    }
//
//    private void showAddMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Меню додавання").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        User currentUser = authRepository.getUser();
//        // Отримуємо роль поточного користувача
//        Role currentRole = currentUser.getRole();
//
//        // Отримуємо доступні опції меню для цієї ролі
//        List<AddMenu> availableOptions = AddMenu.getAvailableOptions(currentRole);
//
//        // Виводимо доступні опції
//        for (int i = 0; i < availableOptions.size(); i++) {
//            AddMenu menu = availableOptions.get(i);
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//
//        handleAddMenuChoice();
//    }
//
//
//    private void handleAddMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            User currentUser = authRepository.getUser();
//            Role currentRole = currentUser.getRole();
//            List<AddMenu> availableOptions = AddMenu.getAvailableOptions(currentRole);
//
//            // Перевіряємо, чи вибір користувача є в списку доступних опцій
//            if (choice >= 1 && choice <= availableOptions.size()) {
//                AddMenu selectedMenu = availableOptions.get(choice - 1);
//
//                switch (selectedMenu) {
//                    case ADD_ARTICLE -> {
//                        articleMenuHandler.createArticle();
//                        showAddMenu();
//                    }
//                    case ADD_CATEGORY -> {
//                        categoryMenuHandler.createCategory();
//                        showAddMenu();
//                    }
//                    case ADD_FEEDBACK -> {
//                        feedbackMenuHandler.createFeedback();
//                        showAddMenu();
//                    }
//                    case EXIT -> showMenu();
//                }
//            } else {
//                System.out.println(
//                    Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.")
//                        .reset());
//                showAddMenu(); // Якщо вибір некоректний, показуємо меню ще раз
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showAddMenu();
//        }
//    }
//
//    private void showViewMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Перегляд даних").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//
//        System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть тип перегляду: ").reset());
//        for (int i = 0; i < ViewMenu.values().length; i++) {
//            ViewMenu menu = ViewMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleViewMenuChoice();
//    }
//
//    private void handleViewMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            ViewMenu selectedMenu = ViewMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case VIEW_ARTICLES -> {
//                    articleMenuHandler.viewAllArticles();
//                    showViewMenu();
//                }
//                case VIEW_CATEGORIES -> {
//                    categoryMenuHandler.viewAllCategories();
//                    showViewMenu();
//                }
//                case VIEW_FEEDBACK -> {
//                    feedbackMenuHandler.viewAllFeedbacks();
//                    showViewMenu();
//                }
//                case EXIT -> showMenu();
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showViewMenu();
//        }
//    }
//
//    private void showEditMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Меню редагування").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//
//        // Виведення меню
//        for (int i = 0; i < EditMenu.values().length; i++) {
//            EditMenu menu = EditMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//
//        handleEditMenuChoice();
//    }
//
//    private void handleEditMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//
//            // Перевірка, чи вибраний варіант в межах допустимих значень
//            if (choice < 1 || choice > EditMenu.values().length) {
//                throw new IllegalArgumentException("Некоректний вибір, спробуйте ще раз.");
//            }
//
//            EditMenu selectedMenu = EditMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case EDIT_ARTICLE -> {
//                    articleMenuHandler.updateArticle();
//                    showEditMenu();
//                }
//                case EDIT_CATEGORY -> {
//                    categoryMenuHandler.updateCategory();
//                    showEditMenu();
//                }
//                case EDIT_FEEDBACK -> {
//                    feedbackMenuHandler.updateFeedback();
//                    showEditMenu();
//                }
//                case EXIT -> showMenu();
//            }
//        } catch (NumberFormatException e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Введено нечислове значення. Спробуйте ще раз.")
//                    .reset());
//            showEditMenu();
//        } catch (IllegalArgumentException e) {
//            System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
//            showEditMenu();
//        }
//    }
//
//    private void showDeleteMenu() {
//        clearConsole();
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("   Меню видалення").reset());
//        System.out.println(Ansi.ansi().fg(Ansi.Color.CYAN).a("═".repeat(50)).reset());
//
//        for (int i = 0; i < DeleteMenu.values().length; i++) {
//            DeleteMenu menu = DeleteMenu.values()[i];
//            System.out.printf(
//                String.valueOf(Ansi.ansi().fg(Ansi.Color.GREEN).a(" %d) %s%n").reset()), i + 1,
//                menu.getName());
//        }
//        handleDeleteMenuChoice();
//    }
//
//    private void handleDeleteMenuChoice() {
//        System.out.print(Ansi.ansi().fg(Ansi.Color.YELLOW).a("Оберіть опцію: ").reset());
//        try {
//            int choice = Integer.parseInt(scanner.nextLine());
//            DeleteMenu selectedMenu = DeleteMenu.values()[choice - 1];
//            switch (selectedMenu) {
//                case DELETE_ARTICLE -> {
//                    articleMenuHandler.deleteArticle();
//                    showDeleteMenu();
//                }
//                case DELETE_CATEGORY -> {
//                    categoryMenuHandler.deleteCategory();
//                    showDeleteMenu();
//                }
//                case DELETE_FEEDBACK -> {
//                    feedbackMenuHandler.deleteFeedback();
//                    showDeleteMenu();
//                }
//                case EXIT -> showMenu();
//            }
//        } catch (Exception e) {
//            System.out.println(
//                Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний вибір, спробуйте ще раз.").reset());
//            showDeleteMenu();
//        }
//    }
//
//    private void clearConsole() {
//        System.out.print("\033[H\033[2J");
//        System.out.flush();
//    }
//}
