package com.wqy.server.controller;

import com.wqy.server.service.IndexPageSearchService;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author: wqy
 * @description: 首页功能控制层
 * @version: 1.0
 * @date: 2023/5/22 13:29
 */
@Api(tags = "首页功能")
@RestController
@RequestMapping("/index")
@Slf4j
@CrossOrigin
public class IndexPageSearchController {

    @Resource
    private IndexPageSearchService indexPageSearchService;

    /**
     * 查找可租的车
     *
     * @param data
     * @return
     */
    @PostMapping("/search")
    @ApiOperation("查找可租的车")
    public R search(@RequestBody Map<String,Object> data) {
        return indexPageSearchService.searchCars(data);
    }

}
