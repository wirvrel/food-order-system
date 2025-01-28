package com.zizen.foodorder.presentation.handler;

import com.zizen.foodorder.persistence.entity.enums.Role;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.impl.AuthServiceImpl;
import java.util.Collections;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;

public class AuthMenuHandler {

    private final AuthServiceImpl authService;

    public AuthMenuHandler(AuthServiceImpl authService) {
        this.authService = authService;
    }

    public boolean registerUser() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введіть логін для реєстрації: ");
            String username = scanner.nextLine();

            System.out.print("Введіть пароль для реєстрації: ");
            String password = scanner.nextLine();

            System.out.print("Введіть email для реєстрації: ");
            String email = scanner.nextLine();

            Role role = Role.USER;
            if (authService.register(username, password, email, role)) {
                authService.setUser(authService.getUser());
                System.out.println(
                    Ansi.ansi().fg(Ansi.Color.GREEN).a("Реєстрація успішна!").reset());
                return true;
            } else {
                System.out.println("Спробуйте ще раз.");
            }
        }
    }

    public boolean signIn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введіть ваш логін: ");
            String username = scanner.nextLine();

            System.out.print("Введіть ваш пароль: ");
            String password = scanner.nextLine();

            if (authService.authenticate(username, password)) {
                authService.setUser(authService.getUser());
                return true;
            } else {
                ErrorFormatter.printErrorsInBox(
                    Collections.singletonList("Невірний логін або пароль."));
                String choice;
                while (true) {
                    System.out.print("Бажаєте спробувати ще раз? (y/n): ");
                    choice = scanner.nextLine();
                    if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("n")) {
                        break;
                    } else {
                        ErrorFormatter.printErrorsInBox(Collections.singletonList(
                            "Невірний вибір. Введіть 'y' для спроби ще раз або 'n' для виходу."));
                    }
                }

                if (choice.equalsIgnoreCase("n")) {
                    System.out.println(
                        Ansi.ansi().fg(Ansi.Color.RED).a("Вихід із програми. До побачення!"));
                    return false;
                }
            }
        }
    }
}
