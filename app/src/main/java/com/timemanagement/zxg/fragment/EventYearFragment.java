package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventYearAdapter;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 17/2/22.
 */

public class EventYearFragment extends Fragment implements AbsListView.OnScrollListener {

    private Activity mActivity;
    private ListView lv_year;
    private List<YearDateModel> mYearDateModels;
    private EventYearAdapter eventYearAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;

    private boolean isCurrent = true;

    private final static int NUM_YEAR = 5;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_YEAR+(NUM_YEAR-1)/2;

    private int frontPoint = INIT_POSITION;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_year, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        lv_year = (ListView) mActivity.findViewById(R.id.lv_year);
        ((BaseActivity)mActivity).tv_left.setVisibility(View.GONE);

        initData();
    }

    private void initData(){
        mYearDateModels = getYearList(mCalendar.get(Calendar.YEAR));
        eventYearAdapter = new EventYearAdapter(mActivity, mYearDateModels);
        lv_year.setAdapter(eventYearAdapter);
        lv_year.setSelection(INIT_POSITION);
        lv_year.setOnScrollListener(this);
    }

    public List<YearDateModel> getYearList(int year){
        List<YearDateModel> yearDateModels =  new ArrayList<YearDateModel>();
        if (year>0){
            for (int i = 0; i < NUM_YEAR; i++) {
                if (year-(NUM_YEAR-1)/2+i>0){
                    YearDateModel yearDateModel = new YearDateModel();
                    yearDateModel.setYear(year-(NUM_YEAR-1)/2+i+"");
                    yearDateModel.setMonthDateModels(getMonthList(year-(NUM_YEAR-1)/2+i));
                    yearDateModels.add(yearDateModel);
                }
            }
        }
        return yearDateModels;
    }

    public List<MonthDateModel> getMonthList(int year){
        List<MonthDateModel> list =  new ArrayList<MonthDateModel>();
        if (year>0){
            for (int i = 1; i <= 12; i++) {
                list.add(getMonthDateModel(year, i));
            }
        }
        return list;
    }

    public MonthDateModel getMonthDateModel(int year, int month){
        if (year<0 || month<0 || month>12){
            return null;
        }
        MonthDateModel monthDateModel = new MonthDateModel();
        monthDateModel.setYear(String.valueOf(year));
        monthDateModel.setMonth(String.valueOf(month));

        List<DayDateModel> listDay =  new ArrayList<DayDateModel>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= maxDays; j++) {
            DayDateModel dayDateModel = new DayDateModel();
            dayDateModel.setYear(String.valueOf(year));
            dayDateModel.setMonth(String.valueOf(month));
            dayDateModel.setDay(String.valueOf(j));

            Calendar cal = Calendar.getInstance();
            Date date = new Date(year-1900, month-1, j);
            cal.setTime(date);
            int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            dayDateModel.setWeek(String.valueOf(week_index));
            listDay.add(dayDateModel);
        }
        monthDateModel.setDayDateModels(listDay);
        return monthDateModel;
    }

    private int[] getMonth(int year, int month){
        LogUtils.i("getMonth---", "year:"+year+", month:"+month);
        if (month<=0){
            year = year + month/12 -1;
            month = month%12 +12;
        }else if (month > 12){
            year = year + month/12;
            month = month%12;
        }
        LogUtils.i("getMonth", "year:"+year+", month:"+month);
        return new int[]{year, month};
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem == INIT_POSITION){
            isCurrent = true;
        }else {
            isCurrent = false;
        }

        if (!isFirst) {
            int num;
            int init_year = mCalendar.get(Calendar.YEAR);
            int num_year = firstVisibleItem - INIT_POSITION;
            LogUtils.i("num", "INIT_POSITION"+INIT_POSITION+",firstVisibleItem:"+firstVisibleItem);

            if (firstVisibleItem < frontPoint) {
                int frontYear = init_year+num_year-(NUM_YEAR-1)/2;
                YearDateModel yearDateModel = new YearDateModel();
                yearDateModel.setYear(frontYear+"");
                if ( frontYear <= 0){
                    frontYear = Math.abs(frontYear)+1;
                }
                yearDateModel.setMonthDateModels(getMonthList(frontYear));

                num = (firstVisibleItem - INIT_POSITION) % NUM_YEAR;
                if (num < 0){
                    num = num + NUM_YEAR;
                }
                mYearDateModels.set(num, yearDateModel);
                LogUtils.i("num0", num+"");
            }

            if (firstVisibleItem > frontPoint) {
                int latterYear = init_year+num_year+(NUM_YEAR-1)/2;
                YearDateModel yearDateModel = new YearDateModel();
                yearDateModel.setYear(latterYear+"");
                yearDateModel.setMonthDateModels(getMonthList(latterYear));

                num = (num_year- (NUM_YEAR-(NUM_YEAR-1)/2*2)) % NUM_YEAR;
                if (num < 0){
                    num = num + NUM_YEAR;
                }
                mYearDateModels.set(num, yearDateModel);
                LogUtils.i("num0", num+"");
            }
            frontPoint = firstVisibleItem;
        }else {
            isFirst = false;
        }
    }

    public void gotoToday(){
        if (!isCurrent) {
            List<YearDateModel> yearDateModels = getYearList(mCalendar.get(Calendar.YEAR));
            for (int i = 0; i < NUM_YEAR; i++) {
                mYearDateModels.set(i, yearDateModels.get(i));
            }
            lv_year.setSelection(INIT_POSITION);
        } else {
            ((MainActivity)mActivity).setFragment(-1);
        }
    }
}
