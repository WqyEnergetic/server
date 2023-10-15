package com.wqy.server.vo;

import com.wqy.server.enums.status.MessageInfo;
import com.wqy.server.enums.status.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description: 为前端返回统一的响应格式
 * @version: 1.0
 * @date: 2023/7/14 21:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class R {

    /**
     状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 数据信息
     */
    private Object data;

    //响应成功方法调用
    public static R ok(){
        R r = new R();
        r.setCode(StatusCode.SUCCESS.getCode());
        r.setMessage("成功");
        return r;
    }

    //响应失败方法调用
    public static R error(){
        R r = new R();
        r.setCode(StatusCode.ERROR.getCode());
        r.setMessage(MessageInfo.SELECT_FAIL.getType());
        return r;
    }

    //封装Map<String,Object>类型的数据
    public R setData(String key,Object value){
        Map<String,Object> map = new HashMap<>();
        map.put(key,value);
        this.data = map;
        return this;
    }

    //封装List<Object>类型的数据
    public R setData(List list){
        this.data = list;
        return this;
    }

    //封装Map<String,Object>类型的数据
    public R setData(Map<String,Object> data){
        this.data = data;
        return this;
    }

}
