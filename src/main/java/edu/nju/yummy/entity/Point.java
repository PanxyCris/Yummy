package edu.nju.yummy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.json.JSONObject;

@Data
public class Point {
    private double lng;
    private double lat;

    public Point(String jsonStr){
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JSONObject locationObject = (JSONObject) ((JSONObject) jsonObject.get("result")).get("location");
        lng = (Double) locationObject.get("lng");
        lat = (Double) locationObject.get("lat");
    }
}
