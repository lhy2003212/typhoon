package com.example.entity;

import lombok.Data;

@Data
public class Likes {
    private Integer id;
    //关联id
    private Integer fid;
    //点赞人ID
    private Integer userId;
    //模块
    private String module;
}
