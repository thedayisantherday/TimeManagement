package com.timemanagement.zxg.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.timemanagement.zxg.adapter.YearDateAdapter;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestActivity extends Activity implements AbsListView.OnScrollListener {

    private Activity mActivity;
    private TextView tv_year;
    private GridView gl_year;
    private List<YearDateModel> mYearDateModels;
    private YearDateAdapter yearDateAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;

    private final static int NUM_YEAR = 5;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%12+(NUM_YEAR-1)/2;

    private int frontPoint = INIT_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        tv_year = (TextView) findViewById(R.id.tv_year);
        gl_year = (GridView) findViewById(R.id.gl_year);
        List<List<String>> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<String> strs = new ArrayList<>();
            for (int j = 0; j < 14; j++) {
                strs.add(i*10+j+"");
            }
            strings.add(strs);
        }
        /*GridViewAdapter gridViewAdapter = new GridViewAdapter(this, strings);
        gl_year.setAdapter(gridViewAdapter);*/
        mYearDateModels = getYearList(mCalendar.get(Calendar.YEAR));
        yearDateAdapter = new YearDateAdapter(this, getMonthList(2017));
        gl_year.setAdapter(yearDateAdapter);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /**
     * 设置滚动监听，当滚动到第二个时，跳到地list.size()+2个，滚动到倒数第二个时，跳到中间第二个，setSelection时，
     * 由于listView滚动并未停止，所以setSelection后会继续滚动，不会出现突然停止的现象
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        /*if (!isFirst) {
            int num;
            int init_year = mCalendar.get(Calendar.YEAR);
            int init_month = mCalendar.get(Calendar.MONTH)+1;
            int num_year = (firstVisibleItem - INIT_POSITION)/12;
            int num_month = (firstVisibleItem - INIT_POSITION)%12;
            LogUtils.i("num", "INIT_POSITION"+INIT_POSITION+",firstVisibleItem:"+firstVisibleItem);

            if (firstVisibleItem < frontPoint) {
                int[] start_month =
                    getMonth(init_year + num_year, init_month + num_month-(NUM_MONTH-1)/2);
                MonthDateModel model = getMonthDateModel(start_month[0], start_month[1]);

                num = (firstVisibleItem - INIT_POSITION) % NUM_MONTH;
                if (num < 0){
                    num = num + NUM_MONTH;
                }
                list.set(num, model);
                LogUtils.i("num0", num+"");
            }

            if (firstVisibleItem > frontPoint) {
                int[] start_month =
                        getMonth(init_year + num_year, init_month + num_month+(NUM_MONTH-1)/2);
                MonthDateModel model = getMonthDateModel(start_month[0], start_month[1]);

                num = (firstVisibleItem - INIT_POSITION - (NUM_MONTH-(NUM_MONTH-1)/2*2)) % NUM_MONTH;
                if (num < 0){
                    num = num + NUM_MONTH;
                }
                list.set(num, model);
                LogUtils.i("num1", num+"");
            }
            frontPoint = firstVisibleItem;
        }else {
            isFirst = false;
        }*/
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

}

