package com.example.mapper;

import com.example.entity.Collect;
import org.apache.ibatis.annotations.Param;

public interface CollectMapper {


    Collect selectUserCollect(Collect collect);

    void insert(Collect dbcollect);

    void deleteById(Collect dbcollect);

    int selectByFidAndModule(@Param("fid") Integer fid,@Param("module") String module);
}
