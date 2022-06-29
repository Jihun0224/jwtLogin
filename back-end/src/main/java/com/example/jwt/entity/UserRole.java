package com.example.jwt.entity;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),USER_ROLE("ROLE_USER");

    private String roleName;

    UserRole(String roleName){
        this.roleName=roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
