package com.timemanagement.zxg.utils;

import android.content.res.Resources;
import android.support.v4.widget.ScrollerCompat;
import android.util.DisplayMetrics;

import com.timemanagement.zxg.activities.activitycontrol.MyApplication;

/**资源尺寸dimension相关工具类
 * Created by zxg on 2016/11/4.
 * QQ:1092885570
 */

public class DimensionUtils {

    public static Resources resources = null;
    private static ScrollerCompat mCloseScroller;

    static {
        resources = MyApplication.getInstance().getResources();
    }

    /**
     * 获取resource的尺寸Dimension
     * @param resId
     * @return resId对应的dimension值以dp/px表示时，返回dimension乘以dimen；dimension以px表示时，返回dimension
     */
    public static float getDimension(int resId){
        return resources.getDimension(resId);
    }

    /**
     * 获取resource的尺寸DimensionPixelSize
     * @param resId
     * @return resId对应的dimension值乘以dimen（无论dimension是以dp/sp还是px表示）
     */
    public static float getDimensionPixelSize(int resId){
        mCloseScroller.startScroll(0, 0, 50, 0, 350);
        return resources.getDimensionPixelSize(resId);
    }

    public static DisplayMetrics getDisplayMetrics(){
        return resources.getDisplayMetrics();
    }

    /**
     * 像素px转换成像素密度dp
     * @param pxValue
     * @return
     */
    public static int px2dp(float pxValue){
        final float scale = resources.getDisplayMetrics().density;
        return (int)(pxValue/scale + 0.5f);
    }

    /**
     * 像素密度dp转换成像素px
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue){
        final float scale = resources.getDisplayMetrics().density;
        return (int)(dpValue*scale + 0.5f);
//        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }

    /**
     * 像素px转换成缩放无关像素sp
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return (int)(pxValue/scale + 0.5f);
    }

    /**
     * 缩放无关像素sp转换成像素px
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return (int)(spValue*scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWidthPixels(){
        return resources.getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getHeightPixels(){
        return resources.getDisplayMetrics().heightPixels;
    }
}
