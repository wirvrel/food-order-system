package com.zizen.foodorder.presentation.handler;

import com.jakewharton.fliptables.FlipTable;
import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.entity.impl.Order;
import com.zizen.foodorder.persistence.entity.impl.User;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.CategoryService;
import com.zizen.foodorder.service.FoodItemService;
import com.zizen.foodorder.service.OrderService;
import com.zizen.foodorder.service.impl.AuthServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class OrderMenuHandler {

    private final AuthServiceImpl authService;
    private final OrderService orderService;
    private final FoodItemService foodItemService;
    private final FoodItemMenuHandler foodItemMenuHandler;
    private final Scanner scanner;

    public OrderMenuHandler(OrderService orderService, FoodItemService foodItemService,
        CategoryService categoryService) {
        this.orderService = orderService;
        this.foodItemService = foodItemService;
        this.authService = AuthServiceImpl.getInstance();
        this.foodItemMenuHandler = new FoodItemMenuHandler(foodItemService, categoryService);
        this.scanner = new Scanner(System.in);
    }

    // Перегляд усіх замовлень
    public void viewAllOrders() {
        AnsiConsole.systemInstall();

        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println(
                Ansi.ansi().fg(Ansi.Color.RED).a("Немає доступних замовлень.").reset());
            return;
        }

        String[] headers = {"ID", "Користувач", "Кількість страв", "Загальна ціна"};
        String[][] data = orders.stream()
            .map(order -> new String[]{
                order.getId().toString(),
                order.getUser().getUsername(),
                String.valueOf(order.getItems().size()),
                String.format("%.2f", order.getTotalPrice())
            })
            .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers, data));
        AnsiConsole.systemUninstall();
    }

    // Перегляд замовлень авторизованого користувача
    public void viewMyOrders() {
        User currentUser = authService.getUser(); // Отримуємо поточного користувача

        if (currentUser == null) {
            System.out.println("Ви не авторизовані.");
            return;
        }

        List<Order> userOrders = orderService.getOrdersByUser(currentUser.getUsername());

        if (userOrders.isEmpty()) {
            System.out.println("У вас ще немає замовлень.");
            return;
        }

        String[] headers = {"ID", "Кількість страв", "Загальна ціна"};
        String[][] data = userOrders.stream()
            .map(order -> new String[]{
                order.getId().toString(),
                String.valueOf(order.getItems().size()),
                String.format("%.2f грн", order.getTotalPrice())
            })
            .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers, data));
    }

    // Створення нового замовлення
    public void createOrder() {
        User currentUser = authService.getUser();
        while (true) {
            List<FoodItem> selectedItems = new ArrayList<>();
            foodItemMenuHandler.viewAllFoodItems();
            while (true) {
                System.out.print("Введіть ID страви: ");
                String input = scanner.nextLine();
                if (input.isBlank()) {
                    break;
                }

                try {
                    UUID foodItemId = UUID.fromString(input);
                    FoodItem item = foodItemService.getFoodItemById(foodItemId);
                    if (item == null) {
                        System.out.println("Страву не знайдено.");
                    } else {
                        selectedItems.add(item);
                        System.out.println("Страву додано до замовлення.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Некоректний формат ID.");
                }

                String choice;
                while (true) {
                    System.out.print("Бажаєте додати ще страву? (y/n): ");
                    choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("y")) {
                        break; // Продовжуємо додавати страви
                    } else if (choice.equalsIgnoreCase("n")) {
                        // Завершуємо додавання страв
                        double totalPrice = selectedItems.stream()
                            .mapToDouble(FoodItem::getPrice)
                            .sum(); // Підраховуємо загальну суму
                        System.out.printf("Загальна сума до сплати: %.2f грн\n", totalPrice);
                        break; // Виходимо з циклу додавання страв
                    } else {
                        ErrorFormatter.printErrorsInBox(Collections.singletonList(
                            "Невірний вибір. Введіть 'y' для додавання ще однієї страви або 'n' для завершення."));
                    }
                }

                if (choice.equalsIgnoreCase("n")) {
                    // Завершуємо створення замовлення
                    try {
                        if (selectedItems.isEmpty()) {
                            System.out.println("Не було обрано жодної страви для замовлення.");
                            return;
                        }
                        Order order = new Order(UUID.randomUUID(), currentUser, selectedItems);
                        orderService.addOrder(order);
                        System.out.println("Замовлення створено успішно.");

                        // Вихід з обох циклів
                        return; // Повернення в меню або завершення методу
                    } catch (EntityArgumentException e) {
                        ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                        System.out.println("Спробуйте ще раз.");
                    }
                }
            }
        }
    }

    // Видалення замовлення
    public void deleteOrder() {
        viewAllOrders();
        System.out.print("Введіть ID замовлення: ");
        UUID orderId;
        try {
            orderId = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Некоректний формат ID.");
            return;
        }

        if (orderService.getOrderById(orderId) == null) {
            System.out.println("Замовлення не знайдено.");
            return;
        }

        orderService.deleteOrder(orderId);
        System.out.println("Замовлення видалено успішно.");
    }

    // Пошук замовлень
    public void searchOrders() {
        System.out.print("Введіть ключове слово для пошуку (ім'я користувача): ");
        String keyword = scanner.nextLine();

        List<Order> results = orderService.searchOrders(keyword);

        if (results.isEmpty()) {
            System.out.println("Немає замовлень, що відповідають критеріям пошуку.");
            return;
        }

        String[] headers = {"ID", "Користувач", "Кількість страв", "Загальна ціна"};
        String[][] data = results.stream()
            .map(order -> new String[]{
                order.getId().toString(),
                order.getUser().getUsername(),
                String.valueOf(order.getItems().size()),
                String.format("%.2f", order.getTotalPrice())
            })
            .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers, data));
    }
}
