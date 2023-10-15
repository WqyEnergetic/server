package com.wqy.server.dto;

import lombok.Data;

/**
 * @author: wqy
 * @description: 首页查询功能中，数据库传输给服务器的数据的DTO
 * @version: 1.0
 * @date: 2023/8/23 21:36
 */
@Data
public class IndexCarSearchDTO {

    private String id;

    private String powerType;

    private Integer oilVolume;

    private String name;

    private String emission;

    private Integer rentPrice;

    private String box;

    private Integer sits;

    private String transmission;

    private Double satisfyRate;

    private String carType;

    private boolean isCarpooling;

    private String currentStore;

    private String arrivalStore;

    private String currentCity;

    private String arrivalCity;

    private String location;
}
