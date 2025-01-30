package com.zizen.foodorder.persistence.repository;

import com.google.gson.reflect.TypeToken;
import com.zizen.foodorder.persistence.JsonPathFactory;
import com.zizen.foodorder.persistence.entity.impl.Order;
import com.zizen.foodorder.persistence.exception.JsonFileIOException;
import com.zizen.foodorder.persistence.util.FileUtil;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderRepository {

    private final List<Order> orders;
    private final Path filePath;

    public OrderRepository() {
        this.filePath = JsonPathFactory.ORDERS.getPath();
        this.orders = loadOrders();
    }

    public void add(Order entity) {
        orders.add(entity);
        saveOrders();
    }

    public void delete(Order entity) {
        orders.removeIf(order -> order.getId().equals(entity.getId()));
        saveOrders();
    }

    public List<Order> getAllObjects() {
        return new ArrayList<>(orders);
    }

    public Order getById(UUID id) {
        return orders.stream()
            .filter(order -> order.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Order> find(String value) {
        return orders.stream()
            .filter(
                order -> order.getUser().getUsername().toLowerCase().contains(value.toLowerCase())
                    ||
                    order.getItems().stream().anyMatch(
                        item -> item.getName().toLowerCase().contains(value.toLowerCase())))
            .collect(Collectors.toList());
    }

    private List<Order> loadOrders() {
        try {
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            return FileUtil.loadFromJson(filePath.toString(), listType);
        } catch (JsonFileIOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void saveOrders() {
        try {
            FileUtil.saveToJson(filePath.toString(), orders);
        } catch (JsonFileIOException e) {
            e.printStackTrace();
        }
    }
}
