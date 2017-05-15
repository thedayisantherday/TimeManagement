package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.activities.activitycontrol.MyApplication;
import com.timemanagement.zxg.adapter.ViewPagerAdapter;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.LunarUtils;
import com.timemanagement.zxg.utils.TimeUtils;
import com.timemanagement.zxg.widget.EventContainerView;
import com.timemanagement.zxg.widget.EventDayView;
import com.timemanagement.zxg.widget.EventDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.timemanagement.zxg.utils.LunarUtils.chineseDateFormat;

public class EventDayActivity extends BaseActivity implements View.OnClickListener{

    public static Context mContext;
    private ViewPager vp_date;
    private TextView tv_date_detail, tv_today, tv_repeat/*, tv_out_date*/;
    private EditText et_date_detail;
    private ScrollView sv_event_container;
    public static EventContainerView view_event_container;

    public static EventDialog mEventDialog;

    private List<EventDayView> vp_views = new ArrayList<EventDayView>();
    private List<DayDateModel[]> mDayDateModels = new ArrayList<>();
    private DayDateModel mDayDateModel;

    private ViewPagerAdapter mViewPagerAdapter;

    private int mPosition = 2; //当前banner的位置
    private int offsetWeek; //与当前日期相差多少周

    private Calendar calendar = Calendar.getInstance();

    public static final int EVENT_EDIT = 2;

    public static int mEventDialogCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_day);

        mContext = this;

        initView();
    }

    /**
     * 解决getIntent()取到的intent为旧的intent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DayDateModel _dayDateModel = (DayDateModel) getIntent().getSerializableExtra("dayDateModel");
        if (_dayDateModel != null) {
            calendar.set(Integer.valueOf(_dayDateModel.getYear()),
                    Integer.valueOf(_dayDateModel.getMonth()) - 1, Integer.valueOf(_dayDateModel.getDay()));
            EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);
        }

        EventModel _eventModel = (EventModel) getIntent().getSerializableExtra("eventModel");
        if (_eventModel != null) {
            calendar.set(_eventModel.getDate().getYear() + 1900,
                    _eventModel.getDate().getMonth(), _eventModel.getDate().getDate());
            EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);

            _dayDateModel = new DayDateModel();
            _dayDateModel.setYear(_eventModel.getDate().getYear() + 1900 + "");
            _dayDateModel.setMonth(_eventModel.getDate().getMonth() + 1 + "");
            _dayDateModel.setDay(_eventModel.getDate().getDate() + "");

            int hour1 = _eventModel.getRemind().getHours();
            int minute1 = _eventModel.getRemind().getMinutes();
            int marginTop = view_event_container.offset / 2 + view_event_container.mTotalHeight * (hour1 * 60 + minute1) / EventContainerView.MINITES_OF_DAY;
            scrollTo(marginTop);
        }

        initData(_dayDateModel);
        if (_eventModel == null) {
            gotoCurrent();
        }
    }

    private void initView(){
        vp_date = (ViewPager) findViewById(R.id.vp_date);
        tv_date_detail = (TextView) findViewById(R.id.tv_date_detail);
        tv_date_detail.setOnClickListener(this);
        tv_today = (TextView)findViewById(R.id.tv_today);
        tv_today.setOnClickListener(this);
        tv_repeat = (TextView)findViewById(R.id.tv_repeat);
        tv_repeat.setOnClickListener(this);
//        tv_out_date = (TextView)findViewById(tv_out_date);
//        tv_out_date.setOnClickListener(this);
        et_date_detail = (EditText) findViewById(R.id.et_date_detail);

        for (int i = 0; i < 6; i++) {
            EventDayView eventDayView = new EventDayView(this);
            /**
             * 被点击的item是已经被选中的，则跳转到EventMonthActivity页面；
             * 否则更新导航栏tv_left和ViewPager各子视图被选中的样式
             */
            eventDayView.setOnViewpagerItemClick(new EventDayView.OnViewpagerItemClick() {
                @Override
                public void onViewpagerItemClick(DayDateModel dayDateModel) {
                    mDayDateModel = dayDateModel;
                    getEventOfDate(mDayDateModel);
                    mViewHolder.tv_left.setText(dayDateModel.getYear()+"年"+dayDateModel.getMonth()+"月");
                    for (int j = 0; j < vp_views.size(); j++) {
                        vp_views.get(j).setItemStyle();
                    }
                }
            });
            vp_views.add(eventDayView);
        }
        sv_event_container = (ScrollView) findViewById(R.id.sv_event_container);
        view_event_container = (EventContainerView) findViewById(R.id.view_event_container);
        view_event_container.setOnLongPressListener(new EventContainerView.OnLongPressListener() {
            @Override
            public void onLongPress(float pressX, float pressY) {
                // 为每个事件添加一个弹窗
                mEventDialog = new EventDialog(mContext);
                mEventDialog.setView(view_event_container, 150, (int)pressY);

                // 将长按位置对应的时间传给EventEditActivity
                EventModel _eventModel = new EventModel();
                Date _date = calendar.getTime();
                if (mDayDateModel != null) {
                    _date =new Date(Integer.valueOf(mDayDateModel.getYear())-1900,
                            Integer.valueOf(mDayDateModel.getMonth())-1, Integer.valueOf(mDayDateModel.getDay()));
                }
                _eventModel.setDate(_date);
                float _y = pressY>view_event_container.offset/2 ? pressY-view_event_container.offset/2 : 0;
                int _minutes = (int)(_y / view_event_container.mTotalHeight * EventContainerView.MINITES_OF_DAY);
                int _hour = _minutes / 60;
                int _minute = _minutes % 60;
                _date.setHours(_hour);
                _date.setMinutes(_minute - _minute%5);
                _eventModel.setRemind(_date);
//                _eventModel.setRemindAgain(_date);
                EventEditActivity.startSelf(mContext, 0, _eventModel);
            }
        });
    }

    private void initData(DayDateModel dayDateModel){
        mViewHolder.tv_left.setText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月");

        mPosition = 2;
        offsetWeek = 0;
        mDayDateModels.clear();
        for (int i = 0; i < 4; i++) {
            mDayDateModels.add(DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (i - 1) * 7)));
        }
        if (mViewPagerAdapter == null) {
            mViewPagerAdapter = new ViewPagerAdapter(vp_views, mDayDateModels);
        }
        mViewPagerAdapter.notifyDataSetChanged();
        vp_date.setAdapter(mViewPagerAdapter);
        vp_date.setOffscreenPageLimit(1);
        vp_date.setCurrentItem(2);
        vp_date.addOnPageChangeListener(new EventDayOnPageChangeListener());

        getEventOfDate(dayDateModel);
    }

    /**
     * view_event_container滑动扫到当前时间对于的位置
     */
    private void gotoCurrent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollTo(view_event_container.mCurTimeHeight);
                LogUtils.i("EventDayActivity", "EventDayActivity EventContainerView: "+view_event_container.mTotalHeight);
                LogUtils.i("EventDayActivity", "EventDayActivity sv_event_container: "+sv_event_container.getHeight());
            }
        }, 500);
    }

    /**
     * 第一次进入时，sv_event_container滑动到当前位置；
     * 添加事件后返回时，sv_event_container滑动到事件的位置
     * @param scrollY
     */
    private void scrollTo(int scrollY) {
        if (scrollY > sv_event_container.getHeight()/3) {
            sv_event_container.scrollTo(0, scrollY-sv_event_container.getHeight()/3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left:
                if (mDayDateModel == null || mDayDateModel.getYear().equals("") || mDayDateModel.getMonth().equals("")){
                    Calendar calendar = Calendar.getInstance();
                    EventMonthActivity.startSelf(mContext, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
                } else {
                    EventMonthActivity.startSelf(mContext,
                            Integer.valueOf(mDayDateModel.getYear()), Integer.valueOf(mDayDateModel.getMonth()));
                }
                break;
            case R.id.iv_right:
                EventEditActivity.startSelf(mContext, 0, null);
                break;
            case R.id.iv_right1:
                break;
            case R.id.iv_right2:
                break;
            case R.id.tv_today:
                break;
            case R.id.tv_repeat:
                EventRepeatActivity.startSelf(mContext);
                break;
//            case tv_out_date:
//                EventListActivity.startSelf(mContext);
//                break;
            case R.id.tv_date_detail:
                MyApplication.getInstance().getRemindService().resetRemindNotification();
//                Intent startIntent = new Intent(this, RemindService.class);
//                startService(startIntent); // 启动服务
//                test(2017, 5, 20);
////                new LunarUtils().getLunar(1991, 3, 30);
//                Calendar _calendar = Calendar.getInstance();
//                LogUtils.i("tv_date_detail", ", year:"+_calendar.get(Calendar.YEAR)+
//                        ", month:"+_calendar.get(Calendar.MONTH)+", day:"+_calendar.get(Calendar.DATE)
//                        + ", hour:"+_calendar.get(Calendar.HOUR)+ ", minute:"+_calendar.get(Calendar.MINUTE));
                break;
        }
    }

    private void test (int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        today.set(year, month-1, day);
        LunarUtils lunar = new LunarUtils(today);
        LogUtils.i("LogUtils", "北京时间：" + chineseDateFormat.format(today.getTime())
                + "　农历" + lunar);
    }

    /**
     * 调用带参数返回startActivityForResult时，
     * 两个activity的启动模式都不能是launchMode="singleInstance"
     * @param requestCode
     * @param resultCode
     * @param data
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case EVENT_EDIT:
//                    EventModel _eventModel = (EventModel) data.getSerializableExtra("event_model");
//                    calendar.set(_eventModel.getDate().getYear()+1900,
//                            _eventModel.getDate().getMonth(), _eventModel.getDate().getDate());
//                    EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);
//
//                    DayDateModel dayDateModel = new DayDateModel();
//                    dayDateModel.setYear(_eventModel.getDate().getYear()+1900+"");
//                    dayDateModel.setMonth(_eventModel.getDate().getMonth()+1+"");
//                    dayDateModel.setDay(_eventModel.getDate().getDate()+"");
//                    initData(dayDateModel);
//
//                    int hour1 = _eventModel.getRemind().getHours();
//                    int minute1 = _eventModel.getRemind().getMinutes();
//                    int marginTop = view_event_container.offset/2 + view_event_container.mTotalHeight * (hour1*60+minute1) / EventContainerView.MINITES_OF_DAY;
//                    scrollTo(marginTop);
////                    setEventDialog(_eventModel);
//                    break;
//            }
//        }
//    }

    private void getEventOfDate(DayDateModel dayDateModel) {
        Calendar _calendar = Calendar.getInstance();
        if (dayDateModel != null) {
            for (int i = 2; i < mEventDialogCount; i++) {
                view_event_container.removeView(view_event_container.getChildAt(2));
            }
            _calendar.set(Integer.valueOf(dayDateModel.getYear()),
                    Integer.valueOf(dayDateModel.getMonth())-1, Integer.valueOf(dayDateModel.getDay()));
        }

        // 为当天的所有事件添加一个EventDialog
        List<EventModel> eventModels = new DatabaseUtil(mContext).queryByDate(_calendar.getTime());
        if (eventModels != null && eventModels.size() > 0) {
            for (int i=0; i < eventModels.size(); i++) {
                mEventDialog = new EventDialog(mContext);
                setEventDialog(eventModels.get(i));
                view_event_container.addView(mEventDialog.view_event_dialog);
            }
        }
        mEventDialogCount = view_event_container.getChildCount();
    }

    /**
     * 设置EventDialog的布局
     * @param eventModel
     */
    private void setEventDialog(EventModel eventModel) {
        if (eventModel == null){
            return;
        }
        mEventDialog.mEventModel = eventModel;
        int hour1 = mEventDialog.mEventModel.getRemind().getHours();
        int minute1 = mEventDialog.mEventModel.getRemind().getMinutes();
        int marginTop = view_event_container.offset/2 + view_event_container.mTotalHeight * (hour1*60+minute1) / EventContainerView.MINITES_OF_DAY;
        scrollTo(marginTop);
//        mEventDialog.setView(view_event_container, 150, marginTop);
        if (mEventDialog.mEventModel.getRemindAgain() != null) {
            int hour2 = mEventDialog.mEventModel.getRemindAgain().getHours();
            int minute2 = mEventDialog.mEventModel.getRemindAgain().getMinutes();
            int minutes = (hour2 - hour1) * 60 + minute2 - minute1;
            int _height = view_event_container.mTotalHeight * minutes / EventContainerView.MINITES_OF_DAY;
            mEventDialog.updateView(view_event_container, 150, marginTop, _height, mEventDialog.mEventModel.getTitle());
        } else {
            mEventDialog.updateView(view_event_container, 150, marginTop, 0, mEventDialog.mEventModel.getTitle());
        }
    }

    @Override
    public void initHead(ViewHolder viewHolder) {
        viewHolder.ll_left.setVisibility(View.VISIBLE);
        viewHolder.ll_left.setOnClickListener(this);
        viewHolder.iv_left.setImageResource(R.drawable.head_left);
        viewHolder.tv_left.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setOnClickListener(this);
//        viewHolder.iv_right1.setVisibility(View.VISIBLE);
//        viewHolder.iv_right2.setVisibility(View.VISIBLE);
    }



    public static void startSelf(Context context, DayDateModel dayDateModel, EventModel eventModel){
        Intent intent = new Intent(context, EventDayActivity.class);
        intent.putExtra("dayDateModel", dayDateModel);
        intent.putExtra("eventModel", eventModel);
        context.startActivity(intent);
    }

    class EventDayOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            LogUtils.i("onPageSelected", "offsetWeek"+offsetWeek+", position"+position+", mPosition"+mPosition);
            switch (position){
                case 0:
                    if (mPosition - position == 1){
                        offsetWeek--;
                        mDayDateModels.set(2, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek-1)*7)));
                        mDayDateModel = mDayDateModels.get(3)[EventDayView.mWeekCheck-1];
                        mViewHolder.tv_left.setText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月");
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 1:
                    if (mPosition - position==1){
                        offsetWeek--;
                        mDayDateModels.set(3, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek-1)*7)));
                        mDayDateModel = mDayDateModels.get(0)[EventDayView.mWeekCheck-1];
                        mViewHolder.tv_left.setText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月");
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 2:
                case 3:
                    offsetWeek = offsetWeek + position - mPosition;
                    mDayDateModels.set(position-1+position-mPosition, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+position-mPosition)*7)));
                    mDayDateModel = mDayDateModels.get(position-1)[EventDayView.mWeekCheck-1];
                    mViewHolder.tv_left.setText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月");
                    getEventOfDate(mDayDateModel);
                    break;
                case 4:
                    if (mPosition - position==-1){
                        offsetWeek++;
                        mDayDateModels.set(0, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+1)*7)));
                        mDayDateModel = mDayDateModels.get(position-1)[EventDayView.mWeekCheck-1];
                        mViewHolder.tv_left.setText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月");
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 5:
                    if (mPosition - position == -1){
                        offsetWeek++;
                        mDayDateModels.set(1, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+1)*7)));
                        mDayDateModel = mDayDateModels.get(0)[EventDayView.mWeekCheck-1];
                        mViewHolder.tv_left.setText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月");
                        getEventOfDate(mDayDateModel);
                    }
                    break;
            }
            mPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtils.i("onPageScrollStateChanged state", "state:"+state+",mposition"+mPosition);
            //ViewPager跳转
            if (state == 0) {
                int pageIndex = mPosition;
                if (mPosition == 0) {
                    pageIndex = vp_views.size() - 2;
                } else if (mPosition == vp_views.size() - 1) {
                    pageIndex = 1;
                }
                if (pageIndex != mPosition) {
                    vp_date.setCurrentItem(pageIndex, false);
                }
            }
        }
    }
}
