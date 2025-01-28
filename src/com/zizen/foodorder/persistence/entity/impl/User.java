package com.zizen.foodorder.persistence.entity.impl;

import com.zizen.foodorder.persistence.entity.Entity;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.entity.enums.Role;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import java.util.UUID;
import java.util.regex.Pattern;

public class User extends Entity implements Comparable<User> {

    private final Role role;
    private String password;
    private String email;
    private String username;

    public User(UUID id, String password, String email, String username, Role role) {
        super(id);

        setPassword(password);
        setEmail(email);
        setUsername(username);

        this.role = role;

        // Якщо є помилки, кидаємо виняток
        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        final String templateName = "пароля";

        if (password.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (password.length() < 8) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 8));
        }
        var pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
        if (!pattern.matcher(password).matches()) {
            errors.add(ErrorTemplates.PASSWORD.getTemplate().formatted(templateName));
        }

        // Якщо є помилки, не встановлюємо пароль
        if (!errors.isEmpty()) {
            return;
        }

        // Хешуємо пароль
        this.password = org.mindrot.bcrypt.BCrypt.hashpw(password,
            org.mindrot.bcrypt.BCrypt.gensalt());
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        final String templateName = "логіну";

        if (username.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        if (username.length() < 4) {
            errors.add(ErrorTemplates.MIN_LENGTH.getTemplate().formatted(templateName, 4));
        }
        if (username.length() > 24) {
            errors.add(ErrorTemplates.MAX_LENGTH.getTemplate().formatted(templateName, 24));
        }
        var pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        if (!pattern.matcher(username).matches()) {
            errors.add(ErrorTemplates.ONLY_LATIN.getTemplate().formatted(templateName));
        }

        this.username = username;
    }

    public void setEmail(String email) {
        final String templateName = "електронної пошти";

        if (email.isBlank()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted(templateName));
        }
        var pattern = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        if (!pattern.matcher(email).matches()) {
            errors.add("Поле %s має бути валідною електронною поштою.".formatted(templateName));
        }

        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
            "password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", username='" + username + '\'' +
            ", id=" + id +
            '}';
    }

    @Override
    public int compareTo(User o) {
        return this.username.compareTo(o.username);
    }
}