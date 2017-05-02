package com.timemanagement.zxg.activities.activitycontrol;

import android.app.Activity;

import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理类
 * Created by zxg on 2016/9/26.
 * QQ:1092885570
 */
public class ActivityManager {
    private static final String TAG = ActivityManager.class.getSimpleName();
    private List<Activity> list_activities;
    private static ActivityManager instance;

    private ActivityManager(){
        //用ArrayList模擬Activity棧
        list_activities = new ArrayList<Activity>();
    }

    /**
     * 单例类
     * @return
     */
    public static ActivityManager getInstance(){
        if(null == instance){
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 将activity添加到activity栈中
     * @param activity
     */
    public void addActivity (Activity activity){
        if (list_activities == null) {
            return;
        }
        list_activities.add(activity);
        LogUtils.i(TAG, activity + " is added");
    }

    /**
     * 获取Activity栈的栈顶元素
     * @return
     */
    public Activity getTopActivity(){
        if (list_activities != null && list_activities.size() >= 1){
            return list_activities.get(list_activities.size()-1);
        }
        return null;
    }

    /**
     * 将activity从activity栈中移除,但是被移除的activity并不会销毁
     * @param activity
     */
    public void removeActivity(Activity activity){
        if(list_activities==null || list_activities.size() == 0){
            return;
        }
        if(list_activities.contains(activity)) {
            list_activities.remove(activity);
            LogUtils.i(TAG, activity + " is removed");
        }
    }

    /**
     * 将activity销毁，并在销毁之前从activity栈中移除
     * @param activity
     */
    public void finishActivity(Activity activity){
        if(list_activities==null || list_activities.size() == 0){
            return;
        }
        if(list_activities.contains(activity)) {
            list_activities.remove(activity);
            activity.finish();
            LogUtils.i(TAG, activity + " is finished");
        }
    }

    /**
     * 只保存底部activity
     */
    public synchronized void onlyOneActivity() {
        for (int i = list_activities.size() - 1; i >= 1; i--) {
            Activity _activity = list_activities.get(i);
            removeActivity(_activity);
            _activity.finish();
        }
        LogUtils.i(TAG, "only one activities is activity");
    }

    /**
     * 清除所有activity
     */
    public synchronized void finishAllActivity() {
        for (int i = list_activities.size() - 1; i >= 0; i--) {
            Activity activity = list_activities.get(i);
            removeActivity(activity);
            activity.finish();
        }
        LogUtils.i(TAG, "all activities is finished");
    }

    /**
     * 获取activity栈中activity的数量
     * @return
     */
    public int size(){
        if (list_activities == null){
            return 0;
        }
        return list_activities.size();
    }
}
