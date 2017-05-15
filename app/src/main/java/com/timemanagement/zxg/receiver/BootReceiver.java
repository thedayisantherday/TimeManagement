package com.timemanagement.zxg.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.timemanagement.zxg.service.RemindService;
import com.timemanagement.zxg.utils.LogUtils;

/**
 * Created by zxg on 17/5/12.
 * 接收开机广播，开启定时提醒服务
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            LogUtils.i("BootReceiver", "BootReceiver is started!");
            // 启动开启定时提醒服务
            Intent remindService = new Intent(context, RemindService.class);
            context.startService(remindService);
        }
    }
}
