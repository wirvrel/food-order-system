package com.zizen.foodorder.service.impl;

import com.zizen.foodorder.persistence.entity.impl.Order;
import com.zizen.foodorder.persistence.repository.OrderRepository;
import com.zizen.foodorder.service.OrderService;
import java.util.List;
import java.util.UUID;

public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl() {
        this.orderRepository = new OrderRepository(); // Ініціалізація репозиторію
    }

    @Override
    public List<Order> getOrdersByUser(String username) {
        return orderRepository.getAllObjects().stream()
            .filter(order -> order.getUser().getUsername().equalsIgnoreCase(username))
            .toList();
    }

    @Override
    public void addOrder(Order order) {
        orderRepository.add(order); // Додаємо замовлення до репозиторію
    }

    @Override
    public void deleteOrder(UUID id) {
        Order order = orderRepository.getById(id);
        if (order != null) {
            orderRepository.delete(order);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.getAllObjects(); // Повертаємо всі замовлення
    }

    @Override
    public List<Order> searchOrders(String keyword) {
        return orderRepository.find(keyword); // Повертаємо замовлення, що містять ключове слово
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.getById(id); // Повертаємо замовлення за ID
    }
}
