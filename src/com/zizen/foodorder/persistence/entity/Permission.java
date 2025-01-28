package com.zizen.foodorder.persistence.entity;

public record Permission(
    boolean canAdd,
    boolean canEdit,
    boolean canDelete,
    boolean canRead) {

}