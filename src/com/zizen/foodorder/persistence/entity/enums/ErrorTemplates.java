package com.zizen.foodorder.persistence.entity.enums;

public enum ErrorTemplates {
    REQUIRED("Поле %s є обов'язковим до заповнення."),
    MIN_LENGTH("Поле %s не може бути меншим за %d симв."),
    MAX_LENGTH("Поле %s не може бути більшим за %d симв."),
    ONLY_LATIN("Поле %s має містити лише латинські символи та символ _."),
    PASSWORD(
        "Поле %s має містити латинські символи, хочаб одна буква з великої, одна з малої та хочаб одна цифра."),
    USER_EXISTS("Користувач із логіном \"%s\" вже існує.");

    private final String template;

    ErrorTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
