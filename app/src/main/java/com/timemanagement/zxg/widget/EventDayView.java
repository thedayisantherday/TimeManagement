package com.timemanagement.zxg.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.Calendar;

/**
 * DayEvent中的ViewPager的子view
 *
 * Created by zxg on 17/3/10.
 */

public class EventDayView extends LinearLayout implements View.OnClickListener{

    private LinearLayout[] ll_days = new LinearLayout[7];
    private TextView[] tv_days = new TextView[7];
    private TextView[] tv_lunars = new TextView[7];

    private DayDateModel[] mDayDateModels;

    //当前日期
    public final static Calendar mCalendar = Calendar.getInstance();
    //当前日期的星期
    public final static int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
    //被选中的星期
    public static int mWeekCheck = mWeek;

    private OnViewpagerItemClick onViewpagerItemClick;

    /**
     * Viewpager的Item点击监听事件
     * @param onViewpagerItemCLick
     */
    public void setOnViewpagerItemClick(OnViewpagerItemClick onViewpagerItemCLick){
        onViewpagerItemClick = onViewpagerItemCLick;
    }
    public interface OnViewpagerItemClick{
        /**
         * @param dayDateModel 被点击item对于的model数据源
         */
        void onViewpagerItemClick(DayDateModel dayDateModel/*, boolean isCheckedClick*/);
    }

    public EventDayView(Context context) {
        // TODO Auto-generated constructor stub
        this(context,null);
    }
    public EventDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        //在构造函数中将Xml中定义的布局解析出来。
        LayoutInflater.from(context).inflate(R.layout.viewpager_item_date, this, true);
        ll_days[0] = (LinearLayout) findViewById(R.id.ll_day0);
        ll_days[1] = (LinearLayout) findViewById(R.id.ll_day1);
        ll_days[2] = (LinearLayout) findViewById(R.id.ll_day2);
        ll_days[3] = (LinearLayout) findViewById(R.id.ll_day3);
        ll_days[4] = (LinearLayout) findViewById(R.id.ll_day4);
        ll_days[5] = (LinearLayout) findViewById(R.id.ll_day5);
        ll_days[6] = (LinearLayout) findViewById(R.id.ll_day6);

        //设置点击事件
        for (int i = 0; i < 7; i++) {
            ll_days[i].setOnClickListener(this);
        }

        tv_days[0] = (TextView) findViewById(R.id.tv_day0);
        tv_days[1] = (TextView) findViewById(R.id.tv_day1);
        tv_days[2] = (TextView) findViewById(R.id.tv_day2);
        tv_days[3] = (TextView) findViewById(R.id.tv_day3);
        tv_days[4] = (TextView) findViewById(R.id.tv_day4);
        tv_days[5] = (TextView) findViewById(R.id.tv_day5);
        tv_days[6] = (TextView) findViewById(R.id.tv_day6);

        tv_lunars[0] = (TextView) findViewById(R.id.tv_lunar0);
        tv_lunars[1] = (TextView) findViewById(R.id.tv_lunar1);
        tv_lunars[2] = (TextView) findViewById(R.id.tv_lunar2);
        tv_lunars[3] = (TextView) findViewById(R.id.tv_lunar3);
        tv_lunars[4] = (TextView) findViewById(R.id.tv_lunar4);
        tv_lunars[5] = (TextView) findViewById(R.id.tv_lunar5);
        tv_lunars[6] = (TextView) findViewById(R.id.tv_lunar6);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_day0:
                setItemBackground(0);
                break;
            case R.id.ll_day1:
                setItemBackground(1);
                break;
            case R.id.ll_day2:
                setItemBackground(2);
                break;
            case R.id.ll_day3:
                setItemBackground(3);
                break;
            case R.id.ll_day4:
                setItemBackground(4);
                break;
            case R.id.ll_day5:
                setItemBackground(5);
                break;
            case R.id.ll_day6:
                setItemBackground(6);
                break;
        }
    }

    /**
     * 点击时，设置item的背景
     */
    private void setItemBackground(int num){

        mWeekCheck = num+1;
        for (int i = 0; i < ll_days.length; i++) {
            if (i == num){
                if (num+1 == mWeek && isCurrentDate(Integer.valueOf(mDayDateModels[i].getYear()),
                        Integer.valueOf(mDayDateModels[i].getMonth()), Integer.valueOf(mDayDateModels[i].getDay()))){
                    ll_days[i].setBackgroundResource(R.drawable.bg_circular_red);
                } else {
                    ll_days[i].setBackgroundResource(R.drawable.bg_circular_blue);
                }
                tv_days[i].setTextColor(Color.WHITE);
                tv_lunars[i].setTextColor(Color.WHITE);
            } else {
                ll_days[i].setBackgroundColor(Color.WHITE);
                if (i==0 || i==6){
                    tv_days[i].setTextColor(getResources().getColor(R.color.color_8888));
                    tv_lunars[i].setTextColor(getResources().getColor(R.color.color_8888));
                } else {
                    tv_days[i].setTextColor(Color.BLACK);
                    tv_lunars[i].setTextColor(Color.BLACK);
                }
            }
        }

        onViewpagerItemClick.onViewpagerItemClick(mDayDateModels[num]);
    }

    /**
     * 更新数据
     * @param dayDateModels
     */
    public void setItemData(DayDateModel[] dayDateModels){
        mDayDateModels = dayDateModels;
        for (int i = 0; i < 7; i++) {
            tv_days[i].setText(mDayDateModels[i].getDay());
        }
        setItemStyle();
    }

    /**
     * 设置item的样式
     */
    public void setItemStyle(){
        if (mDayDateModels == null){
            return;
        }
        for (int i = 0; i < 7; i++) {
            LogUtils.i("setItemData", "mWeekCheck"+mWeekCheck);
            if (mDayDateModels[i].getWeek().equals(mWeekCheck+"")) {
                //星期跟当前日期相同的日期，字体颜色设置为白色
                tv_days[i].setTextColor(Color.WHITE);
                //星期跟当前日期相同的日期，设置背景颜色
                if (isCurrentDate(Integer.valueOf(mDayDateModels[i].getYear()),
                        Integer.valueOf(mDayDateModels[i].getMonth()), Integer.valueOf(mDayDateModels[i].getDay()))){
                    ll_days[i].setBackgroundResource(R.drawable.bg_circular_red);
                } else {
                    ll_days[i].setBackgroundResource(R.drawable.bg_circular_blue);
                }
            } else if (isCurrentDate(Integer.valueOf(mDayDateModels[i].getYear()),
                    Integer.valueOf(mDayDateModels[i].getMonth()), Integer.valueOf(mDayDateModels[i].getDay()))){
                tv_days[i].setTextColor(Color.RED);
                tv_lunars[i].setTextColor(Color.RED);
                ll_days[i].setBackgroundColor(Color.WHITE);
            } else {
                if (i==0 || i==6){
                    tv_days[i].setTextColor(getResources().getColor(R.color.color_8888));
                    tv_lunars[i].setTextColor(getResources().getColor(R.color.color_8888));
                } else {
                    tv_days[i].setTextColor(Color.BLACK);
                    tv_lunars[i].setTextColor(Color.BLACK);
                }
                ll_days[i].setBackgroundColor(Color.WHITE);
            }
        }
    }

    /**
     * 判断给定日期是否为当前日期
     * @param year
     * @param month
     * @param day
     * @return
     */
    private boolean isCurrentDate(int year, int month, int day){
        return (year==mCalendar.get(Calendar.YEAR))
                && (month==mCalendar.get(Calendar.MONTH)+1) && (day==mCalendar.get(Calendar.DATE));
    }
}
