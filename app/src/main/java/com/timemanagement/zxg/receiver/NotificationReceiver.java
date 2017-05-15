package com.timemanagement.zxg.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.timemanagement.zxg.activities.EventDayActivity;
import com.timemanagement.zxg.activities.activitycontrol.MyApplication;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.TimeUtils;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.timemanagement.zxg.activities.EventDayActivity.mContext;

/**
 * Created by zxg on 17/5/11.
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static MyApplication myApplication = MyApplication.getInstance();
    private EventModel eventModel;
    private Bitmap mBitmap;
    private NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        eventModel = (EventModel) intent.getSerializableExtra("remind_eventModel");

        LogUtils.i("NotificationReceiver", eventModel.getTitle());
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(
                    myApplication.getResources(), R.drawable.icon_notification);
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)
                    myApplication.getSystemService(NOTIFICATION_SERVICE);
        }
        sendRemindBroadcast();
    }

    private void sendRemindBroadcast() {
        if (eventModel != null) {
            LogUtils.i("NotificationReceiver111111", eventModel.getTitle());
            Intent intent = new Intent(mContext, EventDayActivity.class);
            PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(mContext)
                    .setContentTitle("TimeManagement")
                    .setContentText(eventModel.getTitle() + "\n" + TimeUtils.dateToStr(eventModel.getRemind()))
                    .setContentIntent(pi)
                    .setSmallIcon(R.drawable.icon_notification_small)
                    .setLargeIcon(mBitmap)
                    .build();
            // 设置提醒方式
            notification.defaults = Notification.DEFAULT_ALL;

//        notification.setLatestEventInfo(mContext, "This is content title", "This is content text", null);
            mNotificationManager.notify(eventModel.getId(), notification);
        }
    }
}
