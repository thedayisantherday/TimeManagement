package com.timemanagement.zxg.utils;

import java.text.DecimalFormat;

/**
 * 数值相关工具类
 * Created by zxg on 2016/10/2.
 * QQ:1092885570
 */
public class NumberUtils {

    /**
     * 保留小数点后几位，四舍五入
     * 当最后一位小数为0时暂时未做处理
     * @param value
     * @param digit 保留小数的位数
     * @return
     * @throws Exception
     */
    public static String formatDecimals(double value, int digit) {
        try {
            if(digit > 0) {
                StringBuffer buffer = new StringBuffer("#,##0.");
                for (int i = 0; i < digit; i++) {
                    buffer.append("0");
                }
                DecimalFormat df = new DecimalFormat(buffer.toString());
                return df.format(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
