package com.zizen.foodorder.service;

import com.zizen.foodorder.persistence.entity.enums.Role;
import com.zizen.foodorder.persistence.entity.impl.User;

public interface AuthService {

    boolean authenticate(String username, String password); // Вхід

    boolean isAuthenticated(); // Перевірка стану

    User getUser();

    void setUser(User user);

    void logout(); // Вихід

    boolean register(String username, String password, String email, Role role);
}
