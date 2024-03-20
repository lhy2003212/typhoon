package com.example.service;


import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;



    public void set(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        collect.setUserId(currentUser.getId());
        Collect dbcollect=collectMapper.selectUserCollect(collect);
        if (dbcollect==null){
            collectMapper.insert(collect);
        }else {
            collectMapper.deleteById(dbcollect);
        }
    }

    public Collect selectUserCollect(Integer fid,String module){
        Collect collect = new Collect();
        Account currentUser = TokenUtils.getCurrentUser();
        collect.setUserId(currentUser.getId());
        collect.setFid(fid);
        collect.setModule(module);
        return collectMapper.selectUserCollect(collect);
    }



    public int selectByFidAndModule(Integer fid,String module){
        int i=collectMapper.selectByFidAndModule(fid,module);
        return i;
    }
}
