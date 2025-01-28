package com.zizen.foodorder.persistence.repository;

import java.util.List;
import java.util.UUID;

public interface GenericRepository<T> {

    void add(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> getAllObjects();

    T getById(UUID id);

    List<T> find(String value);
}
