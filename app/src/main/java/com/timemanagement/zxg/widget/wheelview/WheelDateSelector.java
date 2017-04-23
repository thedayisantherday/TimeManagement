package com.timemanagement.zxg.widget.wheelview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.widget.wheelview.adapters.NumericWheelAdapter;
import com.timemanagement.zxg.widget.wheelview.listener.OnWheelChangedListener;
import com.timemanagement.zxg.widget.wheelview.view.WheelView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zxg on 17/2/6.
 */

public class WheelDateSelector implements OnWheelChangedListener {

    private static final String TAG = WheelDateSelector.class.getSimpleName();

    private Context mContext;
    public Button btn_wheel_confirm, btn_wheel_cancel;
    public TextView tv_wheel_title;
    private View mView;
    private WheelView mWheelViewYear, mWheelViewMonth, mWheelViewDay;

    private Calendar calendar;

    private final int startYear = 1949;
    private final int endYear = 2100;

    public WheelDateSelector(Context context, View view) {
        this.mContext = context;
        this.mView = view;

        initView();

        initData();
    }

    private void initView(){
        btn_wheel_cancel = (Button) mView.findViewById(R.id.btn_wheel_cancel);
        btn_wheel_confirm = (Button) mView.findViewById(R.id.btn_wheel_confirm);
        tv_wheel_title = (TextView) mView.findViewById(R.id.tv_wheel_title);
        mWheelViewYear = (WheelView) mView.findViewById(R.id.wheel_year);
        mWheelViewYear.setItemViewPadding(100, 0, 0, 0);
        mWheelViewYear.addChangingListener(this);
        mWheelViewMonth = (WheelView) mView.findViewById(R.id.wheel_month);
        mWheelViewMonth.addChangingListener(this);
        mWheelViewDay = (WheelView) mView.findViewById(R.id.wheel_day);
        mWheelViewDay.setItemViewPadding(0, 0, 100, 0);
        mWheelViewDay.addChangingListener(this);
    }

    private void initData(){

        calendar = Calendar.getInstance();

        mWheelViewYear.setViewAdapter(new NumericWheelAdapter(mContext, startYear, endYear));
        mWheelViewYear.setVisibleItems(7);
        mWheelViewYear.setCurrentItem(calendar.get(Calendar.YEAR) - startYear);

        mWheelViewMonth.setViewAdapter(new NumericWheelAdapter(mContext, 01, 12));
        mWheelViewMonth.setVisibleItems(7);
        mWheelViewMonth.setCurrentItem(calendar.get(Calendar.MONTH));

        updateDay();
        mWheelViewDay.setVisibleItems(7);
        mWheelViewDay.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH)-1);
    }

    private void updateDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, startYear + mWheelViewYear.getCurrentItem());
        calendar.set(Calendar.MONTH, mWheelViewMonth.getCurrentItem());

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mWheelViewDay.setViewAdapter(new NumericWheelAdapter(mContext, 01, maxDays));
        int curDay = Math.min(maxDays, mWheelViewDay.getCurrentItem()+1);
        mWheelViewDay.setCurrentItem(curDay - 1, true);
    }

    public void setCurrentDate(Date date) {
        if (date == null){
            return;
        }

        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int day = date.getDate();
        if (year < startYear) {
            mWheelViewYear.setCurrentItem(0);
        } else if (year > endYear){
            mWheelViewYear.setCurrentItem(endYear - startYear);
        } else {
            mWheelViewYear.setCurrentItem(year - startYear);
        }

        if (month < 0) {
            mWheelViewMonth.setCurrentItem(0);
        } else if (month > 11) {
            mWheelViewMonth.setCurrentItem(11);
        } else {
            mWheelViewMonth.setCurrentItem(month);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, startYear + mWheelViewYear.getCurrentItem());
        calendar.set(Calendar.MONTH, mWheelViewMonth.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        if (day < 0){
            mWheelViewDay.setCurrentItem(0);
        } else if (day >= maxDays){
            mWheelViewDay.setCurrentItem(maxDays-1);
        } else {
            mWheelViewDay.setCurrentItem(day-1);
        }
    }

    /**
     * 获取选中的日期
     * @return
     */
    public Calendar getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(mWheelViewYear.getCurrentItem()+1949, mWheelViewMonth.getCurrentItem(), mWheelViewDay.getCurrentItem()+1);
        return calendar;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        updateDay();
    }
}
