package com.timemanagement.zxg.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import java.util.List;

/**
 * 系统相关工具类
 * Created by zxg on 2016/10/5.
 * QQ:1092885570
 */
public class SysUtils {

    public static String TAG = SysUtils.class.getSimpleName();

    /**
     * 判断指定包名的进程是否在运行
     * @param packageName
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        if (Tools.isEmpty(packageName)){
            return false;
        }

        try {
            ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            for (RunningAppProcessInfo rapi : processInfos){
                if (rapi.processName.equals(packageName)){
                    return true;
                }
            }
//            List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(100);
//            for (ActivityManager.RunningTaskInfo rapi : taskInfos){
//                if (rapi.topActivity.getPackageName().equals(packageName)){
//                    return true;
//                }
//                activityManager.killBackgroundProcesses(packageName);
//            }
        }catch (Exception e){
            LogUtils.e(TAG, "判断" + packageName + "是否允许：" + e);
        }
        return false;
    }
}
