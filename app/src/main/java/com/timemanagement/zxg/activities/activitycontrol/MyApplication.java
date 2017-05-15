package com.timemanagement.zxg.activities.activitycontrol;

import android.app.Application;
import android.content.Intent;

import com.timemanagement.zxg.service.RemindService;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.SysUtils;

/**
 * Created by zxg on 2016/10/8.
 * QQ:1092885570
 */
//TODO 应用在其他项目中时，需要修改名字
public class MyApplication extends Application {

    private static MyApplication instance;
    private RemindService mRemindService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public synchronized static MyApplication getInstance() {
        return instance;
    }

    /**
     * 初始化定时提醒服务实例
     */
    private void initRemindService(){
        LogUtils.i("MyApplication", "initRemindService is init");
        if (mRemindService == null) {
            LogUtils.i("MyApplication", "RemindService is null");
            // RemindService服务停止运行时，开启定时提醒服务
            // 正在运行时，将实例保存为全军变量
            if (!SysUtils.isServiceRunning(instance, RemindService.class.getName())){
                Intent startIntent = new Intent(instance, RemindService.class);
                startService(startIntent); // 启动服务
            } else {
                LogUtils.i("MyApplication", "RemindService is running");
                RemindService.saveRemindInstance();
            }
        }
    }

    public RemindService getRemindService(){
        initRemindService();

        return mRemindService;
    }

    public void setRemindService(RemindService remindService){
        mRemindService = remindService;
    }
}
