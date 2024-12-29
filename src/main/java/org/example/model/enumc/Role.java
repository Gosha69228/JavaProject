package org.example.model.enumc;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_MODERATOR, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

    // Можно добавить метод для получения роли по имени, чтобы легко искать по строке
    public static Role fromString(String roleName) {
        try {
            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role: " + roleName);
        }
    }
}
