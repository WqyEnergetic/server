package com.wqy.server;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wqy.server.utils.JsonStringParseUtil;
import org.junit.jupiter.api.Test;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/22 22:34
 */
public class JSONUtilTest {

    @Test
    public void testToString01() {
        String s = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"email\": \"johndoe@example.com\",\n" +
                "  \"address\": {\n" +
                "    \"city\": \"New York\",\n" +
                "    \"zipcode\": \"10001\",\n" +
                "    \"country\": \"USA\"\n" +
                "  },\n" +
                "  \"hobbies\": [\"Reading\", \"Traveling\", \"Photography\"],\n" +
                "  \"isMarried\": false\n" +
                "}";
        //String name = JsonStringParseUtil.parseToString(s, "address");
        String address = JsonStringParseUtil.parseToString(s, "address");
        String country = JsonStringParseUtil.parseToString(address, "country");
        System.out.println(country);
    }

    @Test
    public void testParseTwice() {
        String s = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30,\n" +
                "  \"email\": \"johndoe@example.com\",\n" +
                "  \"address\": {\n" +
                "    \"city\": \"New York\",\n" +
                "    \"zipcode\": \"10001\",\n" +
                "    \"country\": \"USA\"\n" +
                "  },\n" +
                "  \"hobbies\": [\"Reading\", \"Traveling\", \"Photography\"],\n" +
                "  \"isMarried\": false\n" +
                "}";
        String country = JsonStringParseUtil.parseTwice(s, "address", "zipcode");
        System.out.println(country);
    }

    @Test
    public void testParseToInt() {
        String s = "{\n" +
                "  \"name\": \"John Doe\",\n" +
                "  \"age\": 30\n" +
                "}";
        Integer age = JsonStringParseUtil.parseToInt(s, "age");

        System.out.println(age);
    }

    @Test
    public void testJSONToArray() {
        String json = "[\"6:00\", \"8:00\", \"10:00\", \"12:00\", \"14:00\", \"16:00\", \"18:00\"]";
        JSONArray objects = JSON.parseArray(json);

        System.out.println(objects);
    }

}
