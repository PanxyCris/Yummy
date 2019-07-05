package edu.nju.yummy.util;

import edu.nju.yummy.service.APIService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;


public class DistanceUtil {

    private double originLng;

    private double originLat;

    private double destianteLng;

    private double destinateLat;

    public DistanceUtil(double originLng, double originLat, double destianteLng, double destinateLat) {
        this.originLng = originLng;
        this.originLat = originLat;
        this.destianteLng = destianteLng;
        this.destinateLat = destinateLat;

    }

    private static double rad(double d) {
        return d * Math.PI / 180.00; //角度转换成弧度
    }

    /*
     * 根据经纬度计算两点之间的距离（单位米）
     * */
    public static double computeDistance(double longitude1, double latitude1, double longitude2, double latitude2) {

        double Lat1 = rad(latitude1); // 纬度

        double Lat2 = rad(latitude2);

        double a = Lat1 - Lat2;//两点纬度之差

        double b = rad(longitude1) - rad(longitude2); //经度之差

        double s = 2 * Math.asin(Math

                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式

        s = s * 6378137.0;//弧长乘地球半径（半径为米）

        s = Math.round(s * 10000d) / 10000d;//精确距离的数值
        s = s / 1000;//将单位转换为km，如果想得到以米为单位的数据 就不用除以1000
        //四舍五入 保留一位小数
        DecimalFormat df = new DecimalFormat("#.0");

        return Double.parseDouble(df.format(s));

    }

    public static String computeTime(double originLng, double originLat, double destinateLng, double destinateLat) throws UnsupportedEncodingException {
        Map paramsMap = new LinkedHashMap<String, String>();
        paramsMap.put("origin", originLat+","+originLng);
        paramsMap.put("destination", destinateLat + "," + destinateLng);
        paramsMap.put("riding_type","1");
        paramsMap.put("ak", APIService.API_KEY);
        return HttpUtil.getJSONString("/direction/v2/riding/",paramsMap);
    }
}
