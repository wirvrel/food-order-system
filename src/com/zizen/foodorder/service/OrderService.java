package com.zizen.foodorder.service;

import com.zizen.foodorder.persistence.entity.impl.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    // Додає нове замовлення
    void addOrder(Order order);

    // Видаляє замовлення за ID
    void deleteOrder(UUID id);

    // Отримує всі замовлення
    List<Order> getAllOrders();

    List<Order> getOrdersByUser(String username);

    // Пошук замовлень за ключовим словом
    List<Order> searchOrders(String keyword);

    // Отримує замовлення за його ID
    Order getOrderById(UUID id);
}
