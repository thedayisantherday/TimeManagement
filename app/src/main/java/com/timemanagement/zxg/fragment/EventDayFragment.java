package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventEditActivity;
import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.adapter.ViewPagerAdapter;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.DimensionUtils;
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

public class EventDayFragment extends Fragment {

    private Activity mActivity;
    private ViewPager vp_date;
    private TextView tv_date_detail;
    private EditText et_date_detail;
    private ScrollView sv_event_container;

    public static EventContainerView view_event_container;
    public static EventDialog mEventDialog;
    private ViewPagerAdapter mViewPagerAdapter;

    private List<EventDayView> vp_views;
    private List<DayDateModel[]> mDayDateModels = new ArrayList<>();
    private DayDateModel mDayDateModel;

    private Calendar calendar = Calendar.getInstance();
    private int mPosition = 1; //当前banner的位置
    private int offsetWeek; //与当前日期相差多少周
    public static int mEventDialogCount = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_day, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();

        initView();
        initData(mDayDateModel);
        gotoCurrent();
    }

    @Override
    public void onStart() {
        super.onStart();
        // 获取Fragment的参数
        Bundle arguments = getArguments();
        if (arguments == null) {
            gotoToday();
        } else {
            mDayDateModel = (DayDateModel) arguments.getSerializable("dayDateModel");
            if (mDayDateModel != null) {
                calendar.set(Integer.valueOf(mDayDateModel.getYear()),
                        Integer.valueOf(mDayDateModel.getMonth()) - 1, Integer.valueOf(mDayDateModel.getDay()));
                EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);
            }

            EventModel _eventModel = (EventModel) arguments.getSerializable("eventModel");
            if (_eventModel != null) {
                calendar.set(_eventModel.getDate().getYear() + 1900,
                        _eventModel.getDate().getMonth(), _eventModel.getDate().getDate());
                EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);

                if (mDayDateModel == null) {
                    mDayDateModel = new DayDateModel();
                }
                mDayDateModel.setYear(_eventModel.getDate().getYear() + 1900 + "");
                mDayDateModel.setMonth(_eventModel.getDate().getMonth() + 1 + "");
                mDayDateModel.setDay(_eventModel.getDate().getDate() + "");

                int hour1 = _eventModel.getRemind().getHours();
                int minute1 = _eventModel.getRemind().getMinutes();
                int marginTop = view_event_container.offset / 2 + view_event_container.mTotalHeight * (hour1 * 60 + minute1) / EventContainerView.MINITES_OF_DAY;
                scrollTo(marginTop);
            }

            if (mDayDateModel != null) {
                initData(mDayDateModel);
            }
        }
    }

    private void initView(){
        vp_date = (ViewPager) mActivity.findViewById(R.id.vp_date);
        tv_date_detail = (TextView) mActivity.findViewById(R.id.tv_date_detail);
        et_date_detail = (EditText) mActivity.findViewById(R.id.et_date_detail);

        vp_views = new ArrayList<EventDayView>();
        for (int i = 0; i < 6; i++) {
            EventDayView eventDayView = new EventDayView(mActivity);
            /**
             * 被点击的item是已经被选中的，则跳转到EventMonthActivity页面；
             * 否则更新导航栏tv_left和ViewPager各子视图被选中的样式
             */
            eventDayView.setOnViewpagerItemClick(new EventDayView.OnViewpagerItemClick() {
                @Override
                public void onViewpagerItemClick(DayDateModel dayDateModel) {
                    mDayDateModel = dayDateModel;
                    setDateDetail(mDayDateModel);
                    getEventOfDate(mDayDateModel);
                    ((MainActivity)mActivity).setTopLefText(dayDateModel.getYear()+"年"+dayDateModel.getMonth()+"月", View.VISIBLE);
                    for (int j = 0; j < vp_views.size(); j++) {
                        vp_views.get(j).setItemStyle();
                    }
                }
            });
            vp_views.add(eventDayView);
        }
        sv_event_container = (ScrollView) mActivity.findViewById(R.id.sv_event_container);
        view_event_container = (EventContainerView) mActivity.findViewById(R.id.view_event_container);
        view_event_container.setOnLongPressListener(new EventContainerView.OnLongPressListener() {
            @Override
            public void onLongPress(float pressX, float pressY) {
                // 为每个事件添加一个弹窗
                mEventDialog = new EventDialog(mActivity);
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
                EventEditActivity.startSelf(mActivity, 0, _eventModel);
            }
        });
    }

    private void initData(DayDateModel dayDateModel){
        ((MainActivity)mActivity).setTopLefText(calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月", View.VISIBLE);

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

        setDateDetail(mDayDateModel);
        getEventOfDate(dayDateModel);
    }

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
        List<EventModel> eventModels = new DatabaseUtil(mActivity).queryByDate(_calendar.getTime());
        if (eventModels != null && eventModels.size() > 0) {
            int dialogWidth = 0; // 事件对话框的宽度，用于确定重叠的dialog有多少个
            int overlapStart = -100;
            for (int i=0; i < eventModels.size(); i++) {
                mEventDialog = new EventDialog(mActivity);
                if (i>0) {
                    int repeat1 = eventModels.get(i-1).getRemind().getHours()*60 + eventModels.get(i-1).getRemind().getMinutes();
                    int repeatAgain = 20+repeat1;
                    if (eventModels.get(i-1).getRemindAgain() != null) {
                        repeatAgain = eventModels.get(i-1).getRemindAgain().getHours()*60 + eventModels.get(i-1).getRemindAgain().getMinutes();
                    }
                    int repeat2 = eventModels.get(i).getRemind().getHours()*60 + eventModels.get(i).getRemind().getMinutes();
                    if (repeat2 - overlapStart >= 25) {
                        setEventDialog(eventModels.get(i), 0, 0);
                    } else {
                        if (repeat2 - repeat1 >= 15 && repeatAgain - repeat1 >= 0 && repeatAgain - repeat2 >= 0) {
                            int leftMargin = ((RelativeLayout.LayoutParams) view_event_container.getChildAt(2 + i - 1).getLayoutParams()).leftMargin;
                            setEventDialog(eventModels.get(i), leftMargin + 10, 0);
                        } else if (repeat2 - repeat1 < 15 && repeat2 - repeat1 >= 0) {
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view_event_container.getChildAt(2 + i - 1).getLayoutParams();
                            int ratio = dialogWidth / params.width;
                            setEventDialog(eventModels.get(i), DimensionUtils.getWidthPixels() - dialogWidth / (1 + ratio), dialogWidth / (1 + ratio));
                            for (int j = ratio; j >= 1; j--) {
                                RelativeLayout.LayoutParams _params = (RelativeLayout.LayoutParams) view_event_container.getChildAt(2 + i - j).getLayoutParams();
                                _params.width = dialogWidth / (1 + ratio);
                                _params.leftMargin = DimensionUtils.getWidthPixels() - dialogWidth * (1 + j) / (1 + ratio);
                            }
                            view_event_container.addView(mEventDialog.view_event_dialog);
                            continue;
                        } else {
                            setEventDialog(eventModels.get(i), 0, 0);
                        }
                    }
                } else {
                    setEventDialog(eventModels.get(i), 0, 0);
                }
                view_event_container.addView(mEventDialog.view_event_dialog);
                dialogWidth = ((RelativeLayout.LayoutParams)view_event_container.getChildAt(2+i).getLayoutParams()).width;
                overlapStart = eventModels.get(i).getRemind().getHours()*60 + eventModels.get(i).getRemind().getMinutes();
            }
        }
        mEventDialogCount = view_event_container.getChildCount();
    }

    /**
     * 设置EventDialog的布局
     * @param eventModel
     */
    private void setEventDialog(EventModel eventModel, int left, int width) {
        if (eventModel == null){
            return;
        }
        if (left < 150) {
            left = 150;
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
            mEventDialog.updateView(view_event_container, left, marginTop, width, _height, mEventDialog.mEventModel.getTitle());
        } else {
            mEventDialog.updateView(view_event_container, left, marginTop, width, 0, mEventDialog.mEventModel.getTitle());
        }
    }

    private void setDateDetail(DayDateModel dayDateModel) {
        String str_date_detail;
        if (dayDateModel == null) {
            str_date_detail = calendar.get(Calendar.YEAR)+"年"+(calendar.get(Calendar.MONTH)+1)+"月"
                    +calendar.get(Calendar.DAY_OF_MONTH)+"日 " + TimeUtils.getWeek(calendar.get(Calendar.DAY_OF_WEEK)+"");
            str_date_detail += " " + new LunarUtils(calendar).toString();
        } else {
            str_date_detail = dayDateModel.getYear()+"年"+dayDateModel.getMonth()+"月"+dayDateModel.getDay()+"日 "
                    + TimeUtils.getWeek(dayDateModel.getWeek()) + " " + dayDateModel.getLunar();

        }

        tv_date_detail.setText(str_date_detail);
    }

    public void gotoToday () {
        calendar = Calendar.getInstance();
        EventDayView.mWeekCheck = calendar.get(Calendar.DAY_OF_WEEK);
        mDayDateModels.clear();
        for (int i = 0; i < 4; i++) {
            mDayDateModels.add(DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (i - 1) * 7)));
        }
        mViewPagerAdapter.notifyDataSetChanged();
        vp_date.setCurrentItem(2);
        mPosition = 2;
        offsetWeek = 0;
        mDayDateModel = mDayDateModels.get(1)[EventDayView.mWeekCheck - 1];
        ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
    }

    /**
     * view_event_container滑动扫到当前时间对于的位置
     */
    private void gotoCurrent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollTo(view_event_container.mCurTimeHeight);
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
                        setDateDetail(mDayDateModel);
                        ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 1:
                    if (mPosition - position==1){
                        offsetWeek--;
                        mDayDateModels.set(3, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek-1)*7)));
                        mDayDateModel = mDayDateModels.get(0)[EventDayView.mWeekCheck-1];
                        setDateDetail(mDayDateModel);
                        ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 2:
                case 3:
                    LogUtils.i("onPageSelected23", "offsetWeek:"+offsetWeek+",position:"+position+",mPosition:"+mPosition);
                    offsetWeek = offsetWeek + position - mPosition;
                    int _position = position-1+position-mPosition;
                    if (_position < 0) {
                        _position = _position % mDayDateModels.size() + mDayDateModels.size();
                    }
                    mDayDateModels.set(_position, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+position-mPosition)*7)));
                    mDayDateModel = mDayDateModels.get(position-1)[EventDayView.mWeekCheck-1];
                    setDateDetail(mDayDateModel);
                    ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
                    getEventOfDate(mDayDateModel);
                    break;
                case 4:
                    if (mPosition - position==-1){
                        offsetWeek++;
                        mDayDateModels.set(0, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+1)*7)));
                        mDayDateModel = mDayDateModels.get(position-1)[EventDayView.mWeekCheck-1];
                        setDateDetail(mDayDateModel);
                        ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
                        getEventOfDate(mDayDateModel);
                    }
                    break;
                case 5:
                    if (mPosition - position == -1){
                        offsetWeek++;
                        mDayDateModels.set(1, DateModelUtil.getWeekDayDateModels(TimeUtils.standardDate(calendar, (offsetWeek+1)*7)));
                        mDayDateModel = mDayDateModels.get(0)[EventDayView.mWeekCheck-1];
                        setDateDetail(mDayDateModel);
                        ((MainActivity)mActivity).setTopLefText(mDayDateModel.getYear()+"年"+mDayDateModel.getMonth()+"月", View.VISIBLE);
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

    public Bundle getArgBundle () {
        Bundle arguments = new Bundle();
        if (mDayDateModel != null) {
            arguments.putInt("year", Integer.valueOf(mDayDateModel.getYear()));
            arguments.putInt("month", Integer.valueOf(mDayDateModel.getMonth()));
        }
        return arguments;
    }
}
