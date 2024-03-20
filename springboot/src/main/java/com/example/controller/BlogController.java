package com.example.controller;

import com.example.common.Result;
import com.example.entity.Blog;
import com.example.service.BlogService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @PostMapping("/add")
    public Result add(@RequestBody Blog blog){
        blogService.add(blog);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        blogService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        blogService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/updateReadCount/{blogId}")
    public Result updateReadCount(@PathVariable Integer blogId){
        blogService.updateReadCount(blogId);
        return Result.success();
    }

    //更新阅读量
    @PutMapping("/update")
    public Result updateById(@RequestBody Blog blog){
        blogService.updateById(blog);
        return Result.success();
    }

    @GetMapping("/select/{id}")
    public Result selectById(@PathVariable Integer id){
        Blog blog=blogService.selectById(id);
        return Result.success(blog);
    }

    @GetMapping("/selectAll")
    public Result selectAll(@RequestBody Blog blog){
        List<Blog> blogs=blogService.selectAll(blog);
        return Result.success(blogs);
    }

    //查出当前用户发布的博客
    @GetMapping("/selectByUserId")
    public Result selectByUserId(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Blog> page=blogService.selectByUserId(blog,pageNum,pageSize);
        return Result.success(page);
    }

    //查出当前用户点赞的博客
    @GetMapping("/selectLike")
    public Result selectLike(
                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Blog> page=blogService.selectLike(pageNum,pageSize);
        return Result.success(page);
    }

    //查出当前用户收藏的博客
    @GetMapping("/selectCollect")
    public Result selectCollect(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Blog> page=blogService.selectCollect(pageNum,pageSize);
        return Result.success(page);
    }

    @GetMapping("/selectPage")
    public Result selectPage(Blog blog,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize){
        PageInfo<Blog> page=blogService.selectPage(blog,pageNum,pageSize);
        return Result.success(page);
    }

    //榜单
    @GetMapping("/selectTop")
    public Result selectTop(){
        List<Blog> blogs=blogService.selectAll(null);
        blogs = blogs.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(10)
                .collect(Collectors.toList());
        return Result.success(blogs);
    }

    //博客推荐
    @GetMapping("/selectRecommed/{blogId}")
    public Result selectRecommed(@PathVariable Integer blogId){
        Set<Blog> blogSet=blogService.selectRecommed(blogId);
        return Result.success(blogSet);
    }
}
