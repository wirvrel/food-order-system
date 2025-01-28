package com.zizen.foodorder.persistence.entity.enums;

import com.zizen.foodorder.persistence.entity.Permission;
import java.util.Map;

/**
 * Перелік ролей та їхніх прав доступу до різних сутностей у системі.
 */
public enum Role {
    ADMIN("Admin", Map.of(
        EntityName.FOOD_ITEM, new Permission(true, true, true, true),
        EntityName.CATEGORY, new Permission(true, true, true, true),
        EntityName.ORDER, new Permission(true, true, true, true),
        EntityName.USER, new Permission(true, true, true, true)
    )),
    USER("User", Map.of(
        EntityName.FOOD_ITEM, new Permission(false, false, false, true),
        EntityName.CATEGORY, new Permission(false, false, false, true),
        EntityName.ORDER, new Permission(true, false, false, true),
        EntityName.USER, new Permission(false, false, false, false)
    ));

    private final String name;

    Role(String name, Map<EntityName, Permission> permissions) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
