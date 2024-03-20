package com.example.mapper;

import com.example.entity.Blog;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BlogMapper {
    void add(Blog blog);

    void deleteById(Integer id);

    void updateById(Blog blog);

    Blog selectById(Integer id);

    List<Blog> selectAll(Blog blog);

    List<Blog> selectUserBlogCount(Integer userId);

    @Update("update blog set read_count=read_count+1 where id=#{blogId}")
    void updateReadCount(Integer blogId);

    List<Blog> selectLike(Integer userId);

    List<Blog> selectCollect(Integer userId);
}
