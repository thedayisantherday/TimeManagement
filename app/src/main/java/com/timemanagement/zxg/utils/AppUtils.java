package com.timemanagement.zxg.utils;

/**
 * Created by zxg on 17/4/18.
 */

public class AppUtils {
    private String[] hours;
    private String[] minutes;

    public String[] getMinutes() {
        if (minutes == null || minutes.length == 0) {
            for (int i = 0; i < 12; i++) {
                minutes[i]=i*5+" 分";
                if (minutes[i].length() == 3){
                    minutes[i] = "0"+minutes[i];
                }
            }
        }
        return minutes;
    }

    public String[] getHours() {
        if (hours == null || hours.length == 0) {
            for (int i = 0; i < 12; i++) {
                hours[i]=i+" 时";
                if (hours[i].length() == 3){
                    hours[i] = "0"+hours[i];
                }
                hours[i+12]=(i+12)+" 时";
            }
        }
        return hours;
    }

}
