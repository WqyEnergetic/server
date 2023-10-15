package com.wqy.server;

import com.wqy.server.pojo.City;
import com.wqy.server.service.OpeningStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: wqy
 * @description:
 * @version: 1.0
 * @date: 2023/7/23 15:13
 */
@SpringBootTest
public class OpeningStoresTests {

    @Resource
    private OpeningStoreService openingStoreService;

    @Test
    public void test(){
        List<City> cities = openingStoreService.searchAllCities();

        // Create a map to group cities by their initials
        Map<Character, List<Map<String, Object>>> cityMap = new HashMap<>();

        for (int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            char initial = city.getInitial();

            // Check if the initial is already present in the cityMap
            if (!cityMap.containsKey(initial)) {
                cityMap.put(initial, new ArrayList<>());
            }

            // Create a map for each city and add it to the respective initial's list
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("id", city.getId());
            cityData.put("name", city.getName());
            cityMap.get(initial).add(cityData);
        }

        // Create the final result list
        List<Map<String, Object>> result = new ArrayList<>();
        for (char initial = 'A'; initial <= 'Z'; initial++) {
            List<Map<String, Object>> citiesForInitial = cityMap.getOrDefault(initial, new ArrayList<>());

            if (!citiesForInitial.isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("initial", String.valueOf(initial));
                map.put("cities", citiesForInitial);
                result.add(map);
            }
        }

        System.out.println(result);
    }
}
