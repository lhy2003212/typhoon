package com.example.controller;


import com.example.common.Result;
import com.example.entity.Collect;
import com.example.service.CollectService;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource
    private CollectService collectService;

    @PostMapping("/set")
    private Result set(@RequestBody Collect collect){
        collectService.set(collect);
        return Result.success();
    }
}
