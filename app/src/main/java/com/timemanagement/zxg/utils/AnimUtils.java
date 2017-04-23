package com.timemanagement.zxg.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

/** support V4中的ActivityOptionsCompat动画效果的使用
 *  android 5.0以后才有动画效果，兼容5.0之前的版本，无动画效果但也不闪退
 *
 * Created by zxg on 2016/11/3.
 * QQ:1092885570
 */

public class AnimUtils {

    private static String TAG = AnimUtils.class.getSimpleName();

    /**
     * 开启活动，界面切换时的滑动动画
     * 若需要所有动画通用，可传入动画id
     * @param activity
     */
    public static void doCustomAnim(Activity activity, Class<? extends Activity> toActivity,
                                    int enterResId, int exitResId){
        Intent intent = new Intent(activity, toActivity);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeCustomAnimation(activity, enterResId, exitResId);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
        LogUtils.i(TAG, "ActivityOptionsCompat.makeCustomAnimation");
    }

    /**
     * 开启活动，界面切换时的放大动画
     * @param view
     */
    public static void doScaleUpAnim(Activity activity, Class<? extends Activity> toActivity,
                                     View view, int startX, int startY, int startWidth, int startHeight){
        Intent intent = new Intent(activity, toActivity);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeScaleUpAnimation(
                view,   //The View that the new activity is animating from
                startX, startY,     //拉伸开始的坐标
                startWidth, startHeight);   //拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        ActivityCompat.startActivity(activity, intent, options.toBundle());
        LogUtils.i(TAG, "startScaleUpAnim");
    }

    /**
     * 开启活动，界面切换时的转换动画
     * @param activity
     * @param view
     * @param toActivity 切换后的界面
     */
    public static void doSceneTransitionAnim(Activity activity,
                                             Class<? extends Activity> toActivity, View view, String transitionTag){
        Intent intent = new Intent(activity, toActivity);
        //view：关联的view，第三个参数"transition_tag"：关联的Tag，同时需要在被关联的view中加上android:transitionName属性
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, view, transitionTag);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
        LogUtils.i(TAG, "startScaleUpAnim");
    }
}
