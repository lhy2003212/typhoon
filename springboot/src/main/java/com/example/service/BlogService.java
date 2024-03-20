package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.BlogMapper;
import com.example.mapper.LikesMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private CollectService collectService;

    @Resource
    private LikesService likesService;

    @Resource
    private UserService userService;

    public void add(Blog blog) {
        blog.setDate(DateUtil.today());
        Account account= TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(account.getRole())){
            blog.setUserId(account.getId());
        }

        blogMapper.add(blog);
    }

    public void deleteById(Integer id) {
        blogMapper.deleteById(id);
    }

    public void updateById(Blog blog) {
        blogMapper.updateById(blog);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id:ids) {
            this.deleteById(id);
        }
    }

    public Blog selectById(Integer id) {
        int beLikes=0;//当前用户被点赞次数
        int beCollect=0;//当前用户被收藏次数
        Blog blog=blogMapper.selectById(id);
        User user = userService.selectById(blog.getUserId());
        List<Blog> userBlogList=blogMapper.selectUserBlogCount(user.getId());
        user.setBlogCount(userBlogList.size());
        for (Blog item:userBlogList) {
            beLikes += likesService.selectByFidAndModule(item.getId(), LikesModuleEnum.BlOG.getValue());
            beCollect += collectService.selectByFidAndModule(item.getId(), LikesModuleEnum.BlOG.getValue());
        }
        user.setUserLikesCount(beLikes);
        user.setUserCollectCount(beCollect);

        blog.setUser(user);
        //当前博客点赞情况
        Integer likesCount = likesService.selectByFidAndModule(id, LikesModuleEnum.BlOG.getValue());
        blog.setLikesCount(likesCount);
        Likes userLikes = likesService.selectUserLikes(id, LikesModuleEnum.BlOG.getValue());
        blog.setUserLike(userLikes!=null);
        //当前博客收藏情况
        Integer collectCount=collectService.selectByFidAndModule(id,LikesModuleEnum.BlOG.getValue());
        blog.setCollectCount(collectCount);
        Collect userCollect = collectService.selectUserCollect(id, LikesModuleEnum.BlOG.getValue());
        blog.setUserCollect(userCollect!=null);

//        //更新当前博客的阅读量
//        blog.setReadCount(blog.getReadCount()+1);
//        blogMapper.updateById(blog);
        return blog;
    }

    public List<Blog> selectAll(Blog blog) {
        List<Blog> blogs=blogMapper.selectAll(blog);
        return blogs;
    }

    public PageInfo<Blog> selectPage(Blog blog, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list = blogMapper.selectAll(blog);
        for (Blog item :list) {
            int i = likesService.selectByFidAndModule(blog.getId(), LikesModuleEnum.BlOG.getValue());
            item.setLikesCount(i);
        }
        return PageInfo.of(list);
    }

    public Set<Blog> selectRecommed(Integer blogId) {
        Blog blog = blogMapper.selectById(blogId);
        String tags = blog.getTags();
        Set<Blog> blogSet = new HashSet();
        if (!ObjectUtil.isEmpty(tags)){
            List<Blog> blogs = blogMapper.selectAll(null);
            JSONArray tagArr = JSONUtil.parseArray(tags);
            for (Object tag:tagArr) {
                Set<Blog> collect = blogs.stream().filter(b -> b.getTags().contains(tag.toString()) && !blogId.equals(b.getId()))
                        .collect(Collectors.toSet());
                blogSet.addAll(collect);
            }
        }
        Set<Blog> blogs = blogSet.stream().limit(5).collect(Collectors.toSet());
        for (Blog item:blogs) {
            int i = likesService.selectByFidAndModule(item.getId(), LikesModuleEnum.BlOG.getValue());
            item.setLikesCount(i);
        }
        return blogs;
    }

    public void updateReadCount(Integer blogId) {
        blogMapper.updateReadCount(blogId);
    }


    public PageInfo<Blog> selectByUserId(Blog blog, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            blog.setUserId(currentUser.getId());
        }
        return this.selectPage(blog,pageNum,pageSize);
    }

    public PageInfo<Blog> selectLike(Integer pageNum, Integer pageSize) {
        Integer userId=null;
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            userId=currentUser.getId();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list=blogMapper.selectLike(userId);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BlOG.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }

    public PageInfo<Blog> selectCollect(Integer pageNum, Integer pageSize) {
        Integer userId=null;
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            userId=currentUser.getId();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list=blogMapper.selectCollect(userId);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.BlOG.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }
}
