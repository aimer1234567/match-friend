package com.aimer.utils;

import cn.hutool.http.HttpRequest;
import com.google.gson.Gson;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用百度地图SDK，获取经纬度
 */

public class AddressCoding {
    public static List<BigDecimal> parse(String address){
        Map<String, String> fromMap = new HashMap<>();
        fromMap.put("key","f97d523f5e630cc6ad3f153ec204114c");
        fromMap.put("address",address);
        String bodyJSON = HttpRequest
                .get("https://restapi.amap.com/v3/geocode/geo?parameters")
                .formStr(fromMap)
                .timeout(20000)
                .execute()
                .body();
        System.out.println(bodyJSON);
        Gson gson = new Gson();
        GeocodeResponse body= gson.fromJson(bodyJSON, GeocodeResponse.class);
        String location = body.getGeocodes().get(0).getLocation();
        String[] locationSplit = location.split(",");
        List<BigDecimal> locationDecimal = new ArrayList<>();
        locationDecimal.add(BigDecimal.valueOf(Double.parseDouble(locationSplit[0])));
        locationDecimal.add(BigDecimal.valueOf(Double.parseDouble(locationSplit[1])));
        return locationDecimal;
    }
    @Data
    public static class GeocodeResponse {
        private String status;
        private String info;
        private String infocode;
        private String count;
        private List<Geocode> geocodes;
    }
    @Data
    public static class Geocode {
        private String formatted_address;
        private String country;
        private String province;
        private String citycode;
        private String city;
        private String district;
        private List<String> township;
        private Neighborhood neighborhood;
        private Building building;
        private String adcode;
        private List<String> street;
        private List<String> number;
        private String location;
        private String level;
    }

    @Data
    public static class Neighborhood {
        private List<String> name;
        private List<String> type;
    }
    @Data
    public static class Building {
        private List<String> name;
        private List<String> type;
    }
}

