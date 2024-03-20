package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.LikesModuleEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.mapper.ActivityMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动业务处理
 **/
@Service
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;
    
    @Resource
    private ActivitySignService activitySignService;

    @Resource
    private LikesService likesService;
    @Resource CollectService collectService;

    /**
     * 新增
     */
    public void add(Activity activity) {
        activityMapper.insert(activity);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        activityMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            activityMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }


    // 查询出用户报名的活动列表
    public PageInfo<Activity> selectUser(Activity activity, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            activity.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectUser(activity);
        PageInfo<Activity> pageInfo = PageInfo.of(list);
        List<Activity> activityList = pageInfo.getList();
        for (Activity act : activityList) {
            ActivitySign activitySign = activitySignService.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
            act.setIsSign(activitySign!=null);
            act.setIsEnd(DateUtil.parseDate(act.getEnd()).isBefore(new Date()));
//            this.setAct(act, currentUser);
        }
        return pageInfo;
    }
    /**
     * 根据ID查询
     */
    public Activity selectById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        Activity act = activityMapper.selectById(id);

        ActivitySign activitySign = activitySignService.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
        act.setIsSign(activitySign!=null);
        act.setIsEnd(DateUtil.parseDate(act.getEnd()).isBefore(new Date()));

        //更新活动阅读量
//        act.setReadCount(act.getReadCount()+1);
//        activityMapper.updateById(act);

        //更新被点赞量和收藏量
        int likes= likesService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        int collects = collectService.selectByFidAndModule(id, LikesModuleEnum.ACTIVITY.getValue());
        act.setCollects(collects);
        act.setLikes(likes);

        //查看是否已点赞和收藏
        Likes like = likesService.selectUserLikes(id, LikesModuleEnum.ACTIVITY.getValue());
        act.setIsLike(like!=null);
        Collect collect = collectService.selectUserCollect(id, LikesModuleEnum.ACTIVITY.getValue());
        act.setIsCollect(collect!=null);
        return act;
    }

    /**
     * 查询所有
     */
    public List<Activity> selectAll(Activity activity) {
        return  activityMapper.selectAll(activity);
    }

    /**
     * 分页查询
     */
    public PageInfo<Activity> selectPage(Activity activity, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> list = activityMapper.selectAll(activity);
        PageInfo<Activity> pageInfo=PageInfo.of(list);
        List<Activity> activitylist = pageInfo.getList();

        Account currentUser = TokenUtils.getCurrentUser();

        for (Activity act:activitylist) {
            ActivitySign activitySign = activitySignService.selectByActivityIdAndUserId(act.getId(), currentUser.getId());
            act.setIsSign(activitySign!=null);
            act.setIsEnd(DateUtil.parseDate(act.getEnd()).isBefore(new Date()));
        }

        return PageInfo.of(list);
    }

    /**
     * 热门活动
     */
    public List<Activity> selectTop() {
        List<Activity> activityList = this.selectAll(null);
        activityList = activityList.stream().sorted((b1, b2) -> b2.getReadCount().compareTo(b1.getReadCount()))
                .limit(2)
                .collect(Collectors.toList());
        return activityList;
    }

    public void updateReadCount(Integer activityId) {
        activityMapper.updateReadCount(activityId);
    }

    public PageInfo<Blog> selectLike(Integer pageNum, Integer pageSize) {
        Integer userId=null;
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            userId=currentUser.getId();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Blog> list=activityMapper.selectLike(userId);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.ACTIVITY.getValue());
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
        List<Blog> list=activityMapper.selectCollect(userId);
        PageInfo<Blog> pageInfo = PageInfo.of(list);
        List<Blog> blogList = pageInfo.getList();
        for (Blog b : blogList) {
            int likesCount = likesService.selectByFidAndModule(b.getId(), LikesModuleEnum.ACTIVITY.getValue());
            b.setLikesCount(likesCount);
        }
        return pageInfo;
    }
}