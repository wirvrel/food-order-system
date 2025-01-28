package com.zizen.foodorder.presentation.handler;

import com.jakewharton.fliptables.FlipTable;
import com.zizen.foodorder.persistence.entity.impl.Category;
import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.CategoryService;
import com.zizen.foodorder.service.FoodItemService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class FoodItemMenuHandler {

    private final FoodItemService foodItemService;
    private final CategoryService categoryService;
    private final CategoryMenuHandler categoryMenuHandler;
    private final Scanner scanner;

    public FoodItemMenuHandler(FoodItemService foodItemService, CategoryService categoryService) {
        this.categoryMenuHandler = new CategoryMenuHandler(categoryService);
        this.foodItemService = foodItemService;
        this.categoryService = categoryService;
        this.scanner = new Scanner(System.in);
    }

    public void viewAllFoodItems() {
        AnsiConsole.systemInstall();

        List<FoodItem> foodItems = foodItemService.getAllFoodItems();

        if (foodItems.isEmpty()) {
            System.out.println(
                Ansi.ansi().fg(Ansi.Color.RED).a("Немає доступних продуктів.").reset());
            return;
        }

        String[] headers = {"ID", "Назва", "Опис", "Ціна", "Категорія"};
        String[][] data = foodItems.stream()
            .map(foodItem -> new String[]{
                foodItem.getId().toString(),
                foodItem.getName(),
                foodItem.getDescription(),
                String.valueOf(foodItem.getPrice()),
                foodItem.getCategory() != null ? foodItem.getCategory().getName() : "Без категорії"
            })
            .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers, data));
        AnsiConsole.systemUninstall();
    }

    public void createFoodItem() {
        while (true) {
            System.out.print("Введіть назву продукту: ");
            String name = scanner.nextLine();
            System.out.print("Введіть опис продукту: ");
            String description = scanner.nextLine();
            System.out.print("Введіть ціну продукту: ");
            double price = Double.parseDouble(scanner.nextLine());

            // Використовуємо CategoryMenuHandler для вибору категорії
            categoryMenuHandler.viewAllCategories();
            System.out.print("Введіть ID категорії: ");
            Category category = null;
            try {
                UUID categoryId = UUID.fromString(scanner.nextLine());
                category = categoryService.getCategoryById(categoryId); // Отримуємо категорію
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректний формат ID.");
            }

            if (category == null) {
                System.out.println("Категорія не знайдена. Спробуйте ще раз.");
                continue;
            }

            try {
                FoodItem foodItem = new FoodItem(UUID.randomUUID(), name, description, price,
                    category);
                foodItemService.addFoodItem(foodItem);
                System.out.println("Продукт створено успішно.");
                break;

            } catch (EntityArgumentException e) {
                ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    public void updateFoodItem() {
        viewAllFoodItems();

        while (true) {
            System.out.print("Введіть ID продукту для оновлення: ");
            UUID id;
            try {
                id = UUID.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректний формат ID. Спробуйте ще раз.");
                continue;
            }

            FoodItem foodItem = foodItemService.getFoodItemById(id);

            if (foodItem == null) {
                System.out.println("Продукт з таким ID не знайдено.");
                continue;
            }

            System.out.print("Введіть нову назву продукту (поточна: " + foodItem.getName() + "): ");
            String newName = scanner.nextLine();
            System.out.print(
                "Введіть новий опис продукту (поточний: " + foodItem.getDescription() + "): ");
            String newDescription = scanner.nextLine();
            System.out.print("Введіть нову ціну продукту (поточна: " + foodItem.getPrice() + "): ");
            double newPrice;
            try {
                newPrice = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ціна повинна бути числом. Спробуйте ще раз.");
                continue;
            }

            categoryMenuHandler.viewAllCategories();
            System.out.print("Введіть ID нової категорії (поточна: " +
                (foodItem.getCategory() != null ? foodItem.getCategory().getName()
                    : "Без категорії") + "): ");
            Category newCategory = null;
            try {
                UUID categoryId = UUID.fromString(scanner.nextLine());
                newCategory = categoryService.getCategoryById(categoryId);
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректний формат ID.");
            }

            if (newCategory == null) {
                System.out.println("Категорія не знайдена. Спробуйте ще раз.");
                continue;
            }

            try {
                foodItem.setName(newName);
                foodItem.setDescription(newDescription);
                foodItem.setPrice(newPrice);
                foodItem.setCategory(newCategory);
                foodItemService.updateFoodItem(foodItem);
                System.out.println("Продукт оновлено успішно.");
                break;
            } catch (EntityArgumentException e) {
                ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    public void deleteFoodItem() {
        viewAllFoodItems();

        while (true) {
            System.out.print("Введіть ID продукту для видалення: ");
            UUID id;
            try {
                id = UUID.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Некоректний формат ID. Спробуйте ще раз.");
                continue;
            }

            FoodItem foodItem = foodItemService.getFoodItemById(id);

            if (foodItem == null) {
                System.out.println("Продукт з таким ID не знайдено.");
                continue;
            }

            foodItemService.deleteFoodItem(id);
            System.out.println("Продукт видалено успішно.");
            break;
        }
    }

    public void searchFoodItems() {
        System.out.print("Введіть ключове слово для пошуку: ");
        String keyword = scanner.nextLine();

        List<FoodItem> results = foodItemService.searchFoodItems(keyword);

        if (results.isEmpty()) {
            System.out.println("Продукти, що відповідають критеріям пошуку, не знайдено.");
            return;
        }

        String[] headers = {"ID", "Назва", "Опис", "Ціна", "Категорія"};
        String[][] data = results.stream()
            .map(foodItem -> new String[]{
                foodItem.getId().toString(),
                foodItem.getName(),
                foodItem.getDescription(),
                String.valueOf(foodItem.getPrice()),
                foodItem.getCategory() != null ? foodItem.getCategory().getName() : "Без категорії"
            })
            .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers, data));
    }


}
