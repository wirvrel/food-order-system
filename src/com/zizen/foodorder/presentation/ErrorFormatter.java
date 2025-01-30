package com.zizen.foodorder.presentation;

import org.fusesource.jansi.Ansi;

public class ErrorFormatter {

    public static void printErrorsInBox(java.util.List<String> errorMessages) {
        String longestError = errorMessages.stream()
            .max(java.util.Comparator.comparingInt(String::length)).orElse("");
        int boxWidth = longestError.length() + 4;
        String boxLine = "•".repeat(boxWidth);  // Використовуємо інші символи для обрамлення

        System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("┌" + boxLine + "┐")
            .reset());  // Заміна на символ "┌"

        for (String errorMessage : errorMessages) {
            int padding = boxWidth - errorMessage.length() - 2;
            int leftPadding = padding / 2;
            int rightPadding = padding - leftPadding;

            String paddingSpaces = " ".repeat(leftPadding);
            String rightPaddingSpaces = " ".repeat(rightPadding);

            System.out.println(Ansi.ansi().fg(Ansi.Color.RED)  // Колір помилок змінено на червоний
                .a("│ " + paddingSpaces + errorMessage + rightPaddingSpaces + " │")
                .reset());  // Заміна на символ "│"
        }

        System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("└" + boxLine + "┘")
            .reset());  // Заміна на символ "└"
    }
}
