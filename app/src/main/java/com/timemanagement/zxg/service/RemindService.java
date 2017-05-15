package com.timemanagement.zxg.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.timemanagement.zxg.activities.activitycontrol.MyApplication;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.receiver.NotificationReceiver;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zxg on 17/5/11.
 * 定时提醒服务，每两个小时查询一次全部两个小时内的全部事件
 */

public class RemindService extends Service {
    // 两个小时的时长
    private static final int INTERVAL = 2 * 60 * 60 * 1000 / 4;

    private static Context mContext;
    private DatabaseUtil databaseUtil;
    //定义定时器
    private Timer timer = new Timer();
    //定义定时任务
    private TimerTask task = new TimerTask(){
        public void run(){
            getEventModel();
        }
    };
    // 用于取消定时任务
    private List<PendingIntent> senders = new ArrayList<>();
    private AlarmManager mAlarmManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        LogUtils.i("RemindService", "RemindService onCreate");
        saveRemindInstance();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        LogUtils.i("RemindService", "Name:"+RemindService.class.getName()+",simpleName:"+RemindService.class.getSimpleName());
        // 开启定时器
        timer.schedule(task, 0, INTERVAL);
    }

    // 从数据库获取两个小时内的全部数据
    private void getEventModel() {
        if (databaseUtil == null) {
            databaseUtil = new DatabaseUtil(mContext);
        }
        // 重置
        senders.clear();

        Calendar _calendar = Calendar.getInstance();
        // 获取两个小时内的全部事件
        List<EventModel> eventModels = databaseUtil.queryByInterval(
                _calendar.getTime(), new Date(_calendar.getTimeInMillis()+INTERVAL));
        LogUtils.i("RemindService", eventModels.toString());

        if (eventModels != null) {
            for (int i = 0; i < eventModels.size(); i++) {
                // 开启定时通知
                sendRemindNotification(eventModels.get(i));
            }
        }
    }

    // 发送提醒通知
    public void sendRemindNotification(EventModel eventModel) {
        LogUtils.i("RemindService1111111", eventModel.getTitle());
        Intent intent = new Intent(mContext, NotificationReceiver.class);
        intent.putExtra("remind_eventModel", eventModel);

        PendingIntent sender = PendingIntent.getBroadcast(mContext,
                eventModel.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        senders.add(sender);

        long triggerAtMillis = eventModel.getRemind().getTime();
        if (eventModel.getRepeat() != 0) {
            triggerAtMillis = eventModel.getRemind().getTime()
                    + (TimeUtils.getNowDateShort().getTime() - TimeUtils.strToDateShort(TimeUtils.dateToStrShort(eventModel.getRemind())).getTime());
        }

        if (mAlarmManager == null) {
            mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        }
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(eventModel.getRemind());
//
//        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, sender);
    }

    /**
     * 重置定时提醒通知
     */
    public void resetRemindNotification() {
        for (int i = 0; i < senders.size(); i++) {
            mAlarmManager.cancel(senders.get(i));
        }

        getEventModel();
    }

    /**
     * 将RemindServices实例保存为全局变量
     */
    public static void saveRemindInstance(){
        LogUtils.i("RemindService", "RemindService saveRemindInstance");

        // 将定时提醒服务实例保存到Application，可以全局调用
        MyApplication.getInstance().setRemindService((RemindService) mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
