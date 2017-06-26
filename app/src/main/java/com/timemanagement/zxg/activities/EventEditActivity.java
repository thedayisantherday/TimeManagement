package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.ActivityManager;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.activities.activitycontrol.MyApplication;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.TimeUtils;
import com.timemanagement.zxg.utils.Tools;
import com.timemanagement.zxg.widget.CustomDialog;
import com.timemanagement.zxg.widget.EventDialog;
import com.timemanagement.zxg.widget.wheelview.WheelContentSelector;
import com.timemanagement.zxg.widget.wheelview.WheelDateSelector;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.timemanagement.zxg.activities.EventDayActivity.mEventDialog;

public class EventEditActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_title, et_comment;
    private RelativeLayout rl_date, rl_remind, rl_remind_again, rl_emergency,
            rl_importance, rl_repeat, rl_repeat_end;
    private TextView tv_date, tv_remind, tv_remind_again, tv_emergency,
            tv_importance, tv_repeat, tv_repeat_end, tv_delete;

    private View view_repeat_end;

    private PopupWindow mPopupWindow, mDatePopupWindow;
    private View mView, mViewDate;
    private WheelDateSelector mWheelDateSelector;
    private WheelContentSelector mWheelContentSelector;

    private static final int TYPE_DATE=0, TYPE_REMIND=1, TYPE_REMIND_AGAIN=2,
            TYPE_EMERGENCY=3, TYPE_IMPORTANCE=4, TYPE_REPEAT=5, TYPE_REPEAT_END=6;
    private int popupWindowType;

    private String[] emergencys = {"非常紧急", "紧急", "一般"/*, "不紧急"*/};
    private String[] importances = {"非常重要", "重要", "一般"/*, "不重要"*/};
    private String[] repeats = {"永不", "每天", "工作日", "每周", "每月", "每年"/*, "自定义"*/};

    private String[] hours = new String[24];
    private String[] minutes = new String[12];

    // 0:新增，1:修改
    private int mType;
    private EventModel mEventModel;

    private DatabaseUtil mDatabaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_edit);

        initView();
        initData();
    }

    private void initView(){
        et_title = (EditText) findViewById(R.id.et_title);

        rl_date = (RelativeLayout) findViewById(R.id.rl_date);
        rl_date.setOnClickListener(this);
        tv_date = (TextView) findViewById(R.id.tv_date);

        rl_remind = (RelativeLayout) findViewById(R.id.rl_remind);
        rl_remind.setOnClickListener(this);
        tv_remind = (TextView) findViewById(R.id.tv_remind);

        rl_remind_again = (RelativeLayout) findViewById(R.id.rl_remind_again);
        rl_remind_again.setOnClickListener(this);
        tv_remind_again = (TextView) findViewById(R.id.tv_remind_again);

        rl_emergency = (RelativeLayout) findViewById(R.id.rl_emergency);
        rl_emergency.setOnClickListener(this);
        tv_emergency = (TextView) findViewById(R.id.tv_emergency);

        rl_importance = (RelativeLayout) findViewById(R.id.rl_importance);
        rl_importance.setOnClickListener(this);
        tv_importance = (TextView) findViewById(R.id.tv_importance);

        rl_repeat = (RelativeLayout) findViewById(R.id.rl_repeat);
        rl_repeat.setOnClickListener(this);
        tv_repeat = (TextView) findViewById(R.id.tv_repeat);

        view_repeat_end = findViewById(R.id.view_repeat_end);

        rl_repeat_end = (RelativeLayout) findViewById(R.id.rl_repeat_end);
        rl_repeat_end.setOnClickListener(this);
        tv_repeat_end = (TextView) findViewById(R.id.tv_repeat_end);

        et_comment = (EditText) findViewById(R.id.et_comment);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);
    }

    private void initData(){
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", 0);
        mEventModel = (EventModel)intent.getSerializableExtra("eventModel");

        if (mType == 0){
            mViewHolder.tv_title.setText("新建事件");
            mViewHolder.tv_right.setText("添加");
            tv_delete.setVisibility(View.GONE);
        } else {
            mViewHolder.tv_title.setText("编辑事件");
            mViewHolder.tv_right.setText("完成");
        }

        if (mEventModel != null){
            et_title.setText(mEventModel.getTitle());
            tv_date.setText(TimeUtils.dateToStrShort(mEventModel.getDate()));
            if (mEventModel.getRemind() == null) {
                tv_remind.setText("无");
            } else {
                tv_remind.setText(TimeUtils.timeToStrShort1(mEventModel.getRemind()));
            }
            if (mEventModel.getRemindAgain() == null) {
                tv_remind_again.setText("无");
            } else {
                tv_remind_again.setText(TimeUtils.timeToStrShort1(mEventModel.getRemindAgain()));
            }
            if (mEventModel.getEmergency() < 0){
                tv_emergency.setText(emergencys[2]);
            } else {
                tv_emergency.setText(emergencys[mEventModel.getEmergency() % 4]);
            }
            if (mEventModel.getImportance() < 0) {
                tv_importance.setText(importances[2]);
            } else {
                tv_importance.setText(importances[mEventModel.getImportance() % 4]);
            }
            tv_repeat.setText(repeats[mEventModel.getRepeat()]);
            if (mEventModel.getRepeat() != 0) {
                view_repeat_end.setVisibility(View.VISIBLE);
                rl_repeat_end.setVisibility(View.VISIBLE);
                if (mEventModel.getRepeatEnd() != null) {
                    tv_repeat_end.setText(TimeUtils.dateToStrShort(mEventModel.getRepeatEnd()));
                }
            }
            et_comment.setText(mEventModel.getComment());
        } else {
            mEventModel = new EventModel();

            // 为每个事件添加一个弹窗
            mEventDialog = new EventDialog(EventDayActivity.mContext);
            mEventDialog.setView(EventDayActivity.view_event_container, 150, 100);
        }

        for (int i = 0; i < 12; i++) {
            hours[i]=i+" 时";
            if (hours[i].length() == 3){
                hours[i] = "0"+hours[i];
            }
            hours[i+12]=(i+12)+" 时";

            minutes[i]=i*5+" 分";
            if (minutes[i].length() == 3){
                minutes[i] = "0"+minutes[i];
            }
        }
    }

    @Override
    public void initHead(ViewHolder viewHolder) {
        viewHolder.iv_left.setVisibility(View.GONE);
        viewHolder.tv_left.setVisibility(View.VISIBLE);
        viewHolder.tv_left.setOnClickListener(this);
        viewHolder.tv_left.setText("取消");
        viewHolder.tv_title.setVisibility(View.VISIBLE);
        viewHolder.tv_right.setVisibility(View.VISIBLE);
        viewHolder.tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Tools.closeKeyboard(view, mContext);
        switch (view.getId()){
            case R.id.rl_date:
                openWheelDateSelector(TYPE_DATE);
                mWheelDateSelector.setCurrentDate(mEventModel.getDate());
                break;
            case R.id.rl_remind:
                if ("无".equals(tv_remind.getText().toString())) {
                    openWheelContentSelector(Arrays.asList(hours), Arrays.asList(minutes), hours[8], minutes[0], TYPE_REMIND);
                } else {
                    String[] _reminds = tv_remind.getText().toString().split(":");
                    openWheelContentSelector(Arrays.asList(hours), Arrays.asList(minutes),
                            hours[Integer.valueOf(_reminds[0])], minutes[Integer.valueOf(_reminds[1])/5], TYPE_REMIND);
                }
                mWheelContentSelector.setWheelViewCyclic(true);
                mWheelContentSelector.setWheelViewCyclic1(true);
                break;
            case R.id.rl_remind_again:
                if ("无".equals(tv_remind_again.getText().toString())) {
                    openWheelContentSelector(Arrays.asList(hours), Arrays.asList(minutes), hours[8], minutes[0], TYPE_REMIND_AGAIN);
                } else {
                    String[] _remindAgains = tv_remind_again.getText().toString().split(":");
                    openWheelContentSelector(Arrays.asList(hours), Arrays.asList(minutes),
                            hours[Integer.valueOf(_remindAgains[0])], minutes[Integer.valueOf(_remindAgains[1])/5], TYPE_REMIND_AGAIN);
                }
                mWheelContentSelector.setWheelViewCyclic(true);
                mWheelContentSelector.setWheelViewCyclic1(true);
                break;
            case R.id.rl_emergency:
                if (mEventModel.getEmergency() < 0) {
                    openWheelContentSelector(Arrays.asList(emergencys), null, emergencys[2], "", TYPE_EMERGENCY);
                } else {
                    openWheelContentSelector(Arrays.asList(emergencys), null, emergencys[mEventModel.getEmergency() % 4], "", TYPE_EMERGENCY);
                }
                break;
            case R.id.rl_importance:
                if (mEventModel.getImportance() < 0) {
                    openWheelContentSelector(Arrays.asList(importances), null, importances[2], "", TYPE_IMPORTANCE);
                } else {
                    openWheelContentSelector(Arrays.asList(importances), null, importances[mEventModel.getImportance() % 4], "", TYPE_IMPORTANCE);
                }
                break;
            case R.id.rl_repeat:
                openWheelContentSelector(Arrays.asList(repeats), null, repeats[mEventModel.getRepeat() % repeats.length], "", TYPE_REPEAT);
                break;
            case R.id.rl_repeat_end:
                openWheelDateSelector(TYPE_REPEAT_END);
                mWheelDateSelector.setCurrentDate(mEventModel.getRepeatEnd());
                break;
            case R.id.tv_delete:
                final EventEditActivity self = this;
                CustomDialog dialog = new CustomDialog(mContext, "删除事件", "确定要删除该事件吗？", "取消", "确定");
                dialog.showDialog();
                dialog.setClickCallBack(new CustomDialog.ClickCallBack() {
                    @Override
                    public void onNegative() {
                        LogUtils.i(TAG, "Negative button is clicked!");
                    }

                    @Override
                    public void onPositive() {
                        EventDayActivity.view_event_container.removeView(
                                EventDayActivity.mEventDialog.view_event_dialog);
                        if (mDatabaseUtil == null) {
                            mDatabaseUtil = new DatabaseUtil(mContext);
                        }
                        mDatabaseUtil.deleteData(mEventModel.getId());

                        resetRemindNotification();

                        self.finish();
                    }
                });
                break;
            case R.id.btn_wheel_cancel:
                if (popupWindowType == TYPE_DATE || popupWindowType == TYPE_REPEAT_END){
                    mDatePopupWindow.dismiss();
                } else {
                    mPopupWindow.dismiss();
                }
                break;
            case R.id.btn_wheel_confirm:
                switch (popupWindowType){
                    case TYPE_DATE:
                        tv_date.setText(TimeUtils.dateToStrShort(mWheelDateSelector.getCurrentDate().getTime()));
                        mEventModel.setDate(mWheelDateSelector.getCurrentDate().getTime());
                        mDatePopupWindow.dismiss();
                        return;
                    case TYPE_REMIND:
                        tv_remind.setText(mWheelContentSelector.getCurrentItem().substring(0,2)
                                + ":" + mWheelContentSelector.getCurrentItem1().substring(0,2));
//                        Date remind = (Date)mEventModel.getDate().clone();
//                        remind.setHours(Integer.valueOf(mWheelContentSelector.getCurrentItem().substring(0,2)));
//                        remind.setMinutes(Integer.valueOf(mWheelContentSelector.getCurrentItem1().substring(0,2)));
//                        mEventModel.setRemind(remind);
                        mPopupWindow.dismiss();
                        break;
                    case TYPE_REMIND_AGAIN:
                        tv_remind_again.setText(mWheelContentSelector.getCurrentItem().substring(0,2)
                                + ":" + mWheelContentSelector.getCurrentItem1().substring(0,2));
                        mPopupWindow.dismiss();
                        break;
                    case TYPE_EMERGENCY:
                        tv_emergency.setText(mWheelContentSelector.getCurrentItem());
                        mEventModel.setEmergency(mWheelContentSelector.getCurrentItemID());
                        mPopupWindow.dismiss();
                        break;
                    case TYPE_IMPORTANCE:
                        tv_importance.setText(mWheelContentSelector.getCurrentItem());
                        mEventModel.setImportance(mWheelContentSelector.getCurrentItemID());
                        mPopupWindow.dismiss();
                        break;
                    case TYPE_REPEAT:
                        tv_repeat.setText(mWheelContentSelector.getCurrentItem());
                        mEventModel.setRepeat(mWheelContentSelector.getCurrentItemID());
                        if (mWheelContentSelector.getCurrentItemID() == 0){
                            view_repeat_end.setVisibility(View.GONE);
                            rl_repeat_end.setVisibility(View.GONE);
                        } else {
                            view_repeat_end.setVisibility(View.VISIBLE);
                            rl_repeat_end.setVisibility(View.VISIBLE);
                        }
                        mPopupWindow.dismiss();
                        break;
                    case TYPE_REPEAT_END:
                        tv_repeat_end.setText(TimeUtils.dateToStrShort(mWheelDateSelector.getCurrentDate().getTime()));
                        mEventModel.setRepeatEnd(mWheelDateSelector.getCurrentDate().getTime());
                        mDatePopupWindow.dismiss();
                        break;
                }
                break;
            case R.id.tv_left:
                if (mType == 0) {
                    EventDayActivity.view_event_container.removeView(
                            EventDayActivity.mEventDialog.view_event_dialog);
                }
//                this.finish();
                EventDayActivity.startSelf(mContext, null, null);
                ActivityManager.getInstance().finishActivity(mthis);
                break;
            case R.id.tv_right:
                if (checkData()) {
                    //将事件数据保存到数据库
                    if (mDatabaseUtil == null) {
                        mDatabaseUtil = new DatabaseUtil(mContext);
                    }
                    if (mType == 0) {
                        mDatabaseUtil.insertReturnLatest(mEventModel);
                        EventDayActivity.mEventDialogCount++;
                    } else {
                        mDatabaseUtil.updateData(mEventModel);
                    }

                    resetRemindNotification();

                    EventDayActivity.startSelf(mContext, null, mEventModel);
//                    Intent intent = new Intent(mContext, EventDayActivity.class);
//                    intent.putExtra("event_model", mEventModel);
//                    ((EventEditActivity)mContext).setResult(RESULT_OK, intent);
//                    this.finish();
                    ActivityManager.getInstance().finishActivity(mthis);
                }
                break;
        }
    }

    // 重置定时提醒任务
    private void resetRemindNotification() {
        if (MyApplication.getInstance().getRemindService() != null) {
            MyApplication.getInstance().getRemindService().resetRemindNotification();
        }
    }

    /**
     * 检查输入数据的有效性
     * @return true：有效，false：无效
     */
    private boolean checkData(){
        //未输入标题时，默认为"新建事件"
        String str_title = et_title.getText().toString();
        if (Tools.isEmpty(str_title)){
            Tools.showToast("请输入【标题】");
            return false;
        }
        mEventModel.setTitle(str_title);

        //未选择日期时，默认为当前日期
        if (mEventModel.getDate() == null) {
            mEventModel.setDate(TimeUtils.getNowDateShort());
        }

        //提醒时间为空时，返回false数据无效
        if ("无".equals(tv_remind.getText().toString())){
            Tools.showToast("请选择【提醒】时间！");
            return false;
        } else {
            String[] str_repeat= tv_remind.getText().toString().split(":");
            Date remind = (Date)mEventModel.getDate().clone();
            remind.setHours(Integer.valueOf(str_repeat[0]));
            remind.setMinutes(Integer.valueOf(str_repeat[1]));
            mEventModel.setRemind(remind);
        }
        if (!"无".equals(tv_remind_again.getText().toString())) {
            String[] str_repeat_again= tv_remind_again.getText().toString().split(":");
            Date remind_again = (Date)mEventModel.getDate().clone();
            remind_again.setHours(Integer.valueOf(str_repeat_again[0]));
            remind_again.setMinutes(Integer.valueOf(str_repeat_again[1]));
            mEventModel.setRemindAgain(remind_again);
        }
        if (mEventModel.getRemindAgain() != null && mEventModel.getRemindAgain().before(mEventModel.getRemind())) {
            Tools.showToast("【再次提醒】必须在【提醒】之前！");
            return false;
        }

        //未选择紧急程度时，默认为2——"一般"
        if (mEventModel.getEmergency() == -1) {
            mEventModel.setEmergency(2);
        }
        //未选择重要程度时，默认为2——"一般"
        if (mEventModel.getImportance() == -1) {
            mEventModel.setImportance(2);
        }

        mEventModel.setComment(et_comment.getText().toString());
        mEventModel.setIsFinished(false);

        return true;
    }

    private void openWheelContentSelector(List<String> contents,
                      List<String> contents1, String content, String content1, int type){
        if (mView == null) {
            mView = View.inflate(mContext, R.layout.wheel_content_selector, null);
        }
        if (mPopupWindow == null){
            mPopupWindow = new PopupWindow(mView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setFocusable(true);
            mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mPopupWindow = null;
                }
            });
        }

        mWheelContentSelector = new WheelContentSelector(mContext, mView, contents, contents1);
        mWheelContentSelector.btn_wheel_cancel.setOnClickListener(this);
        mWheelContentSelector.btn_wheel_confirm.setOnClickListener(this);
        if (!Tools.isEmpty(content)){
            mWheelContentSelector.setCurrentItem(content);
        }
        if (content1 != null && content1.length()>0 && !Tools.isEmpty(content1)){
            mWheelContentSelector.setCurrentItem1(content1);
        }
        popupWindowType = type;
    }

    private void openWheelDateSelector(int type){
        if (mViewDate == null) {
            mViewDate = View.inflate(mContext, R.layout.wheel_date_selector, null);
        }
        if (mDatePopupWindow == null){
            mDatePopupWindow = new PopupWindow(mViewDate,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDatePopupWindow.setFocusable(true);
            mDatePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            mDatePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mDatePopupWindow = null;
                }
            });
        }

        mWheelDateSelector = new WheelDateSelector(mContext, mViewDate);
        mWheelDateSelector.btn_wheel_cancel.setOnClickListener(this);
        mWheelDateSelector.btn_wheel_confirm.setOnClickListener(this);
        popupWindowType = type;
    }

    /**
     * @param context
     * @param type // 0:新增，1:修改
     * @param eventModel
     */
    public static void startSelf(Context context, int type, EventModel eventModel){
        LogUtils.i("EventEditActivity startSelf", context.toString());
        Intent intent = new Intent(context, EventEditActivity.class);
        // 解决点击事件EventViewDialog，跳转EventEditActivity报错的问题，但是也会有问题
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", type);
        intent.putExtra("eventModel", eventModel);
        context.startActivity(intent);
    }
}
