package edu.nju.yummy.util;

import edu.nju.yummy.entity.Point;
import edu.nju.yummy.service.APIService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class LocateUtil {

    public static Point getLocation(String address) throws UnsupportedEncodingException {
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("address", address);
        paramsMap.put("output", "json");
        paramsMap.put("ak", APIService.API_KEY);
        return new Point(HttpUtil.getJSONString("/geocoder/v2/",paramsMap));
    }
}
