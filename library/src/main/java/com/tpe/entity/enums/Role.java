package com.tpe.entity.enums;

public enum Role {
    MEMBER("MEMBER"),
    EMPLOYEE("EMPLOYEE"),
    ADMIN("ADMIN");

    private final String name;

    public String getName() {
        return name;
    }

    Role(String name) {
        this.name=name;
    }


}
