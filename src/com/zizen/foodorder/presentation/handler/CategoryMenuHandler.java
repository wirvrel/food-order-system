package com.zizen.foodorder.presentation.handler;

import com.jakewharton.fliptables.FlipTable;
import com.zizen.foodorder.persistence.entity.impl.Category;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class CategoryMenuHandler {

    private final CategoryService categoryService;
    private final Scanner scanner;

    public CategoryMenuHandler(CategoryService categoryService) {
        this.categoryService = categoryService;
        this.scanner = new Scanner(System.in);
    }

    public void viewAllCategories() {
        AnsiConsole.systemInstall();

        List<Category> categories = categoryService.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println(
                Ansi.ansi().fg(Ansi.Color.RED).a("Немає доступних категорій.").reset());
            return;
        }

        // Підготовка заголовків та даних
        String[] headers = {"ID", "Назва категорії"};
        String[][] data = categories.stream()
            .map(category -> new String[]{
                category.getId().toString(),
                category.getName()
            })
            .toArray(String[][]::new);

        // Виведення таблиці
        System.out.println(FlipTable.of(headers, data));

        // Закінчення Jansi (важливо для очищення системи)
        AnsiConsole.systemUninstall();
    }

    // Створення нової категорії
    public void createCategory() {
        while (true) {
            System.out.print("Введіть назву категорії: ");
            String name = scanner.nextLine();

            try {
                // Створюємо нову категорію
                Category category = new Category(UUID.randomUUID(), name);
                categoryService.addCategory(category);
                System.out.println("Категорію створено успішно.");
                break; // Вихід із циклу, оскільки відгук успішно створено

            } catch (EntityArgumentException e) {
                // Виводимо помилки
                ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    public void updateCategory() {
        viewAllCategories();

        UUID id;
        Category category;

        while (true) {
            System.out.print("Введіть ID категорії: ");
            try {
                id = UUID.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Некоректний формат ID. Спробуйте ще раз.");
                continue;
            }

            category = categoryService.getCategoryById(id);
            if (category == null) {
                System.out.println("❌ Категорію не знайдено.");
                continue;
            }

            break;
        }

        while (true) {
            System.out.print("Введіть нову назву категорії: ");
            String newName = scanner.nextLine();

            try {
                Category updatedCategory = new Category(category.getId(), newName);
                categoryService.updateCategory(updatedCategory);
                System.out.println("✅ Категорію оновлено успішно.");
                break;
            } catch (EntityArgumentException e) {
                System.out.println("❌ Введено некоректні дані:");
                ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
                System.out.println("Спробуйте ще раз.");
            }
        }
    }


    // Видалення категорії
    public void deleteCategory() {
        viewAllCategories();
        UUID id;
        while (true) {
            System.out.print("Введіть ID категорії: ");
            try {
                id = UUID.fromString(scanner.nextLine()); // Перевірка на коректність формату UUID
                Category category = categoryService.getCategoryById(id);
                if (category == null) {
                    System.out.println("Категорію з таким ID не знайдено.");
                    continue; // Якщо категорію не знайдено, повторюємо введення
                }
                break; // Якщо ID коректний і категорія знайдена, виходимо з циклу
            } catch (IllegalArgumentException e) {
                System.out.println(
                    Ansi.ansi().fg(Ansi.Color.RED).a("Некоректний формат ID, спробуйте ще раз.")
                        .reset());
            }
        }

        // Видаляємо категорію
        categoryService.deleteCategory(id);
        System.out.println("Категорію видалено успішно.");
    }

    // Пошук категорій
    public void searchCategories() {
        System.out.print("Введіть ключове слово: ");
        String keyword = scanner.nextLine();
        List<Category> results = categoryService.searchCategories(keyword);

        // Якщо немає результатів
        if (results.isEmpty()) {
            System.out.println("Немає категорій, що відповідають критеріям пошуку.");
            return;
        }

        // Підготовка заголовків та даних для таблиці
        String[] headers = {"ID", "Категорія"};
        String[][] data = results.stream()
            .map(category -> new String[]{
                category.getId().toString(),
                category.getName()
            })
            .toArray(String[][]::new);

        // Виведення таблиці
        System.out.println(FlipTable.of(headers, data));
    }
}
