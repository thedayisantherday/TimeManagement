package com.timemanagement.zxg.activities.activitycontrol;

import android.app.Application;

/**
 * Created by zxg on 2016/10/8.
 * QQ:1092885570
 */
//TODO 应用在其他项目中时，需要修改名字
public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public synchronized static MyApplication getInstance() {
        return instance;
    }
}
