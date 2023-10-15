package com.wqy.server.controller;

import com.wqy.server.pojo.IllegalDriving;
import com.wqy.server.service.IllegalDrivingService;
import com.wqy.server.utils.JsonStringParseUtil;
import com.wqy.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @author: wqy
 * @description: 处理违章的控制层
 * @version: 1.0
 * @date: 2023/5/13 10:37
 */
@Api(tags = "违章处理")
@RestController
@RequestMapping("/illegalDriving")
@Slf4j
@CrossOrigin
public class illegalDrivingController {

    @Resource
    private IllegalDrivingService illegalDrivingService;

    /**
     * 获取所有违法信息
     *
     * @param data 前端传来的json
     * @return
     */
    @ApiOperation("获取所有违章信息")
    @PostMapping("/record")
    public R illegalDrivingList(@RequestBody String data) {
        log.info("进入illegalDriving方法，获取到的参数:{}", data);

        //若postCode为1，代表客户要查找之前处理的，0代表查找未处理的
        Integer postCode = JsonStringParseUtil.parseToInt(data, "postCode");
        String token = JsonStringParseUtil.parseToString(data, "authorization");

        //获取记录
        List<IllegalDriving> list = illegalDrivingService.selectIllegalDrivingRecord(postCode, token);

        if (list == null) {
            return R.error();
        }

        log.info("前端传来的postCode为:{}", postCode);
        return R.ok().setData(list);
    }

    /**
     * 用户处理违章记录
     *
     * @param data token + 违章id
     * @return
     */
    @ApiOperation("用户处理违章记录")
    @PostMapping("/deal")
    public R deal(@RequestBody Map<String,Object> data) {
        return illegalDrivingService.eliminateIllegalRecord(data);
    }

}


