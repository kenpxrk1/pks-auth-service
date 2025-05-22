package ru.mirea.auth.service.constant;

public enum Role {
    ROLE_USER, ROLE_ADMIN;

    public static Role getDefaultRole() {
        return Role.ROLE_USER;
    }
}
