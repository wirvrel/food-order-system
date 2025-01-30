package com.zizen.foodorder.persistence.entity.impl;

import com.zizen.foodorder.persistence.entity.Entity;
import com.zizen.foodorder.persistence.entity.enums.ErrorTemplates;
import com.zizen.foodorder.persistence.exception.EntityArgumentException;
import java.util.List;
import java.util.UUID;

public class Order extends Entity implements Comparable<Order> {

    private User user;
    private List<FoodItem> items;
    private double totalPrice;

    public Order(UUID id, User user, List<FoodItem> items) {
        super(id);

        setUser(user);
        setItems(items);
        calculateTotalPrice();

        if (!errors.isEmpty()) {
            throw new EntityArgumentException(errors);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("користувача"));
        }
        this.user = user;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public void setItems(List<FoodItem> items) {
        if (items == null || items.isEmpty()) {
            errors.add(ErrorTemplates.REQUIRED.getTemplate().formatted("списку страв"));
        }
        this.items = items;
        calculateTotalPrice(); // Перерахунок ціни після зміни списку
    }

    public double getTotalPrice() {
        return totalPrice;
    }


    private void calculateTotalPrice() {
        this.totalPrice = items.stream().mapToDouble(FoodItem::getPrice).sum();
    }

    @Override
    public String toString() {
        return "Order{" +
            "user=" + user +
            ", items=" + items +
            ", totalPrice=" + totalPrice +
            ", id=" + id +
            '}';
    }

    @Override
    public int compareTo(Order o) {
        return Double.compare(this.totalPrice, o.totalPrice);
    }
}
