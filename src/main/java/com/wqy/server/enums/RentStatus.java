package com.wqy.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/27 23:06
 */
@Getter
@AllArgsConstructor
public enum RentStatus {

    RENT_FINISHED("已完成"),

    NOT_TRAVEL("未出行"),

    RENTING("租赁中");

    private final String type;

}
