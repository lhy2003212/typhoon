package com.example.entity;

import lombok.Data;

import java.util.Objects;

@Data
public class Blog {
    /** ID */
    private Integer id;
    /** 标题 */
    private String title;
    /** 内容 */
    private String content;
    /** 简介 */
    private String descr;
    /** 封面 */
    private String cover;
    /** 标签 */
    private String tags;
    /** 发布人ID */
    private Integer userId;
    /** 发布日期 */
    private String date;
    /** 浏览量 */
    private Integer readCount;

    private Integer categoryId;

    private User user;

    private String categoryName;

    private Integer likesCount;

    //返回当前博客是不是已经被当前用户点赞
    private boolean userLike;

    private Integer collectCount;
    //返回当前博客是不是已经被当前用户收藏
    private boolean userCollect;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blog blog = (Blog) o;
        return id.equals(blog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
