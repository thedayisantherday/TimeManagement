package com.timemanagement.zxg.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
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

    /**
     * 判断指定名称的服务是否在运行
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        if (Tools.isEmpty(serviceName)){
            return false;
        }
        LogUtils.i("SysUtils", serviceName);

        try {
            ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningServiceInfo> serviceInfos = activityManager.getRunningServices(50);
            for (RunningServiceInfo rapi : serviceInfos){
                LogUtils.i("SysUtils", rapi.service.getClassName());
                if (rapi.service.getClassName().equals(serviceName)){
                    return true;
                }
            }
        }catch (Exception e){
            LogUtils.e(TAG, "判断" + serviceName + "是否允许：" + e);
        }
        return false;
    }
}
