package com.example.entity;

import lombok.Data;

@Data
public class User extends Account{
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String avatar;
    private String role;
    private String sex;
    private String phone;
    private String email;
    private String info;
    private String birth;

    private Integer blogCount;
    private Integer userLikesCount;
    private Integer userCollectCount;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role='" + role + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", info='" + info + '\'' +
                ", birth='" + birth + '\'' +
                '}';
    }
}
