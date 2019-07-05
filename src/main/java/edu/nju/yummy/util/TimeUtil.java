package edu.nju.yummy.util;

public class TimeUtil {

    private final static int unit = 60;

    public static String getVisionTime(int inputSecond) {
        int second = inputSecond % unit;
        int inputMinute = inputSecond / unit;
        int minute = inputMinute % unit;
        int hour = inputMinute / unit;
        String returnStr = "";
        if (hour != 0)
            returnStr += hour + "h";
        if (minute != 0)
            returnStr += minute + "min";
        if (hour == 0 && minute == 0)
            returnStr += second + "s";
        return returnStr;
    }
}
