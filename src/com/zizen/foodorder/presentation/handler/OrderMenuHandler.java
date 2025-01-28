package com.zizen.foodorder.presentation.handler;

import com.jakewharton.fliptables.FlipTable;
import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.entity.impl.Order;
import com.zizen.foodorder.persistence.entity.impl.User;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.FoodItemService;
import com.zizen.foodorder.service.OrderService;
import com.zizen.foodorder.service.impl.AuthServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class OrderMenuHandler {

    private final AuthServiceImpl authService;
    private final OrderService orderService;
    private final FoodItemService foodItemService;
    private final Scanner scanner;

    public OrderMenuHandler(OrderService orderService, FoodItemService foodItemService) {
        this.orderService = orderService;
        this.foodItemService = foodItemService;
        this.authService = AuthServiceImpl.getInstance();
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

    // Створення нового замовлення
    public void createOrder() {
        User currentUser = authService.getUser();
        while (true) {
            List<FoodItem> selectedItems = new ArrayList<>();
            while (true) {
                System.out.println("Доступні страви:");
                foodItemService.getAllFoodItems().forEach(item ->
                    System.out.println(
                        item.getId() + " - " + item.getName() + " (" + item.getPrice() + " грн)")
                );

                System.out.print("Введіть ID страви (або залиште порожнім для завершення): ");
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
            }

            if (selectedItems.isEmpty()) {
                System.out.println("Замовлення повинно містити хоча б одну страву.");
                continue;
            }

            try {
                Order order = new Order(UUID.randomUUID(), currentUser, selectedItems);
                orderService.addOrder(order);
                System.out.println("Замовлення створено успішно.");
                break;
            } catch (EntityArgumentException e) {
                ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    // Оновлення замовлення
    public void updateOrder() {
        viewAllOrders();
        System.out.print("Введіть ID замовлення для оновлення: ");
        UUID orderId;
        try {
            orderId = UUID.fromString(scanner.nextLine());
        } catch (IllegalArgumentException e) {
            System.out.println("Некоректний формат ID.");
            return;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            System.out.println("Замовлення не знайдено.");
            return;
        }

        createOrder(); // Можна повторно використовувати логіку створення замовлення
    }

    // Видалення замовлення
    public void deleteOrder() {
        viewAllOrders();
        System.out.print("Введіть ID замовлення для видалення: ");
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
