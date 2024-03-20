package com.example.common.enums;

public enum LikesModuleEnum {
    BlOG("博客"),
    ACTIVITY("活动");

    private String value;

    public String getValue() {
        return value;
    }

    LikesModuleEnum(String value) {
        this.value = value;
    }
}
