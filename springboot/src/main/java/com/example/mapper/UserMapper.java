package com.example.mapper;

import com.example.entity.User;

import java.util.List;

public interface UserMapper {

    /*新增*/
    void insert(User user);

    User selectUserByUserName(String name);

    void deleteById(Integer id);

    void updateById(User user);

    User selectById(Integer id);

    List<User> selectAll(User user);
}
