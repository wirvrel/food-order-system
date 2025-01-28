package com.zizen.foodorder.service.impl;

import com.google.gson.reflect.TypeToken;
import com.zizen.foodorder.persistence.JsonPathFactory;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.entity.enums.Role;
import com.zizen.foodorder.persistence.entity.impl.User;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import com.zizen.foodorder.persistence.exception.JsonFileIOException;
import com.zizen.foodorder.persistence.util.FileUtil;
import com.zizen.foodorder.presentation.ErrorFormatter;
import com.zizen.foodorder.service.AuthService;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mindrot.bcrypt.BCrypt;

public class AuthServiceImpl implements AuthService {

    private static final String USERS_JSON_PATH = JsonPathFactory.USERS.getPath().toString();
    private static AuthServiceImpl instance;
    private final Map<String, User> usersDatabase;
    private User currentUser;

    public AuthServiceImpl() {
        this.usersDatabase = new HashMap<>();
        loadUsersFromJson();
    }

    public static AuthServiceImpl getInstance() {
        if (instance == null) {
            instance = new AuthServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = usersDatabase.get(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            currentUser = user;
            return true;
        }
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    @Override
    public User getUser() {
        return currentUser;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void logout() {
        System.out.println(
            "Користувач вийшов: " + (currentUser != null ? currentUser.getUsername() : "null"));
        this.currentUser = null;
    }

    @Override
    public boolean register(String username, String password, String email, Role role) {
        if (usersDatabase.containsKey(username)) {
            // Використовуємо шаблон помилки з переліку
            ErrorFormatter.printErrorsInBox(Collections.singletonList(
                ErrorTemplates.USER_EXISTS.getTemplate().formatted(username)
            ));
            return false;
        }
        try {

            User newUser = new User(UUID.randomUUID(), password, email, username, role);
            usersDatabase.put(username, newUser);
            saveUsersToJson();
            return true;
        } catch (EntityArgumentException e) {
            ErrorFormatter.printErrorsInBox(new ArrayList<>(e.getErrors()));
            return false;
        }
    }

    private void loadUsersFromJson() {
        Type userListType = new TypeToken<List<User>>() {
        }.getType();
        try {
            List<User> userList = FileUtil.loadFromJson(USERS_JSON_PATH, userListType);
            if (userList != null) {
                for (User user : userList) {
                    usersDatabase.put(user.getUsername(), user);
                }
            }
        } catch (JsonFileIOException e) {
            System.err.println("Помилка при завантаженні користувачів: " + e.getMessage());
        }
    }

    private void saveUsersToJson() {
        List<User> userList = new ArrayList<>(usersDatabase.values());
        try {
            FileUtil.saveToJson(USERS_JSON_PATH, userList);
        } catch (JsonFileIOException e) {
            System.err.println("Помилка при збереженні користувачів: " + e.getMessage());
        }
    }

}
