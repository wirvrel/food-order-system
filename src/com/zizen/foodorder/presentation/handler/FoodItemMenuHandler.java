package com.zizen.foodorder.presentation.handler;

import com.jakewharton.fliptables.FlipTable;
import com.zizen.foodorder.persistence.entity.impl.Category;
import com.zizen.foodorder.persistence.entity.impl.FoodItem;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.CategoryService;
import com.zizen.foodorder.service.FoodItemService;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            List<String> errors = new ArrayList<>(); // Список для збору всіх помилок

            System.out.print("Введіть назву продукту: ");
            String name = scanner.nextLine();

            System.out.print("Введіть опис продукту: ");
            String description = scanner.nextLine();

            System.out.print("Введіть ціну продукту: ");
            double price = 0;
            try {
                price = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                errors.add("Некоректний формат ціни. Введіть число більше 0.");
            }

            // Використовуємо CategoryMenuHandler для вибору категорії
            categoryMenuHandler.viewAllCategories();
            System.out.print("Введіть ID категорії: ");
            Category category = null;
            try {
                UUID categoryId = UUID.fromString(scanner.nextLine());
                category = categoryService.getCategoryById(categoryId);
                if (category == null) {
                    errors.add("❌ Категорія з таким ID не знайдена.");
                }
            } catch (IllegalArgumentException e) {
                errors.add("Некоректний формат ID категорії.");
            }
            if (!errors.isEmpty()) {
                // Відображаємо всі помилки за один раз
                ErrorFormatter.printErrorsInBox(errors);
                System.out.println("Спробуйте ще раз.");
                continue; // Повторний запит на введення
            }

            // Якщо всі дані валідні – додаємо страву
            FoodItem foodItem = new FoodItem(UUID.randomUUID(), name, description, price, category);
            foodItemService.addFoodItem(foodItem);
            System.out.println("✅ Продукт створено успішно.");
            break; // Вихід з циклу після успішного створення
        }
    }

    public void updateFoodItem() {
        viewAllFoodItems();

        UUID id;
        FoodItem foodItem;

        // Вибір продукту для оновлення
        while (true) {
            System.out.print("Введіть ID продукту: ");
            try {
                id = UUID.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Некоректний формат ID. Спробуйте ще раз.");
                continue;
            }

            foodItem = foodItemService.getFoodItemById(id);
            if (foodItem == null) {
                System.out.println("❌ Продукт не знайдено.");
                continue;
            }

            break;
        }

        // Введення нових даних з валідацією
        while (true) {
            System.out.print("Введіть нову назву продукту: ");
            String newName = scanner.nextLine();

            System.out.print("Введіть новий опис продукту: ");
            String newDescription = scanner.nextLine();

            System.out.print("Введіть нову ціну продукту: ");
            double newPrice;
            try {
                newPrice = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Некоректний формат ціни. Введіть число більше 0.");
                continue;
            }

            categoryMenuHandler.viewAllCategories();
            System.out.print("Введіть нове ID категорії: ");
            UUID newCategoryId;
            Category newCategory;
            try {
                newCategoryId = UUID.fromString(scanner.nextLine());
                newCategory = categoryService.getCategoryById(newCategoryId);
                if (newCategory == null) {
                    System.out.println("❌ Категорію не знайдено.");
                    continue;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Некоректний формат ID категорії.");
                continue;
            }

            // Створення нового об'єкта з валідацією
            try {
                FoodItem updatedFoodItem = new FoodItem(foodItem.getId(), newName, newDescription,
                    newPrice, newCategory);
                foodItemService.updateFoodItem(updatedFoodItem);
                System.out.println("✅ Продукт оновлено успішно.");
                break;
            } catch (EntityArgumentException e) {
                System.out.println("❌ Введено некоректні дані:");
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

    public void exportMenuToCSV() {
        // Отримуємо список всіх страв
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();

        // Перевірка на порожнє меню
        if (foodItems.isEmpty()) {
            System.out.println("❌ Меню порожнє, неможливо здійснити експорт.");
            return;
        }

        System.out.print(
            "Введіть шлях до файлу (наприклад: D:\\food_menu.csv) або натисніть Enter для використання стандартного (food_menu.csv): ");
        String filePath = scanner.nextLine().trim();

        // Якщо шлях не введений, використовуємо стандартний
        if (filePath.isEmpty()) {
            filePath = "food_menu.csv";
        }

        File file = new File(filePath);

        // Перевіряємо, чи існує директорія для файлу
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.out.println("❌ Помилка при створенні директорії для файлу.");
                return;
            }
        }

        // Запис у CSV-файл
        try (FileWriter writer = new FileWriter(file)) {
            writer.write('\uFEFF');
            // Запис заголовків
            writer.write("ID,Назва,Опис,Ціна,Категорія\n");

            // Запис кожної страви
            for (FoodItem foodItem : foodItems) {
                writer.write(String.format("%s,%s,%s,%.2f,%s\n",
                    foodItem.getId().toString(),
                    escapeCsvValue(foodItem.getName()),
                    escapeCsvValue(foodItem.getDescription()),
                    foodItem.getPrice(),
                    foodItem.getCategory() != null ? escapeCsvValue(
                        foodItem.getCategory().getName()) : "Без категорії"
                ));
            }

            System.out.println("✅ Меню успішно експортовано у файл");
        } catch (IOException e) {
            System.out.println("❌ Помилка при експорті меню: " + e.getMessage());
        }
    }

    private String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

}
