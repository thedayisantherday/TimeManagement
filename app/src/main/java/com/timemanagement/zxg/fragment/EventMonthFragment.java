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
import android.widget.TextView;

import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventMonthAdapter;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 17/2/9.
 */

public class EventMonthFragment extends Fragment implements AbsListView.OnScrollListener {

    private Activity mActivity;
    private ListView lv_month;
    private TextView tv_month;
    private List<MonthDateModel> mMonthDateModels;
    private EventMonthAdapter eventMonthAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;

    private boolean isCurrent = true;

    private final static int NUM_MONTH = 12;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_MONTH+(NUM_MONTH-1)/2;

    private int frontPoint = INIT_POSITION;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_month, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        tv_month = (TextView) mActivity.findViewById(R.id.tv_month);
        lv_month = (ListView) mActivity.findViewById(R.id.lv_month);

        initData();
    }

    private void initData(){
        Calendar calendar = Calendar.getInstance();
        ((BaseActivity)mActivity).tv_left.setVisibility(View.VISIBLE);
        ((BaseActivity)mActivity).tv_left.setText(calendar.get(Calendar.YEAR)+"年");

        mMonthDateModels = getList(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
        eventMonthAdapter = new EventMonthAdapter(mActivity, mMonthDateModels);
        lv_month.setAdapter(eventMonthAdapter);
        lv_month.setSelection(INIT_POSITION);
        lv_month.setOnScrollListener(this);
    }

    /**
     * 获取数据
     * @return
     */
    public List<MonthDateModel> getList(int year, int month){
        List<MonthDateModel> list =  new ArrayList<MonthDateModel>();
        if (year>0 && month>0 && month<12){
            for (int i = 0; i < NUM_MONTH; i++) {
                if (month-(NUM_MONTH-1)/2+i<=0){
                    if (year<=1){
                        continue;
                    }
                    list.add(getMonthDateModel(year-1, month-(NUM_MONTH-1)/2+i+12));
                } else if (month-(NUM_MONTH-1)/2+i<=12){
                    list.add(getMonthDateModel(year, month-(NUM_MONTH-1)/2+i));
                } else {
                    list.add(getMonthDateModel(year+1, month-(NUM_MONTH-1)/2+i-12));
                }
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
        if (year <= 0){
            year = Math.abs(year)+1;
        }
        LogUtils.i("getMonth", "year:"+year+", month:"+month);
        return new int[]{year, month};
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState){
            case 0:
                tv_month.setVisibility(View.GONE);
                break;
            default:
                tv_month.setVisibility(View.VISIBLE);
                break;
        }
        LogUtils.i("onScrollStateChanged", scrollState+"");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (firstVisibleItem == INIT_POSITION){
            isCurrent = true;
        }else {
            isCurrent = false;
        }

        if (!isFirst) {

            tv_month.setText(eventMonthAdapter.getItem(firstVisibleItem).getYear()+"年"
                + eventMonthAdapter.getItem(firstVisibleItem).getMonth()+"月");

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
                mMonthDateModels.set(num, model);
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
                mMonthDateModels.set(num, model);
                LogUtils.i("num1", num+"");
            }
            frontPoint = firstVisibleItem;
        }else {
            isFirst = false;
        }
    }

    public void gotoToday(){
        if (!isCurrent) {
            List<MonthDateModel> monthDateModels = getList(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
            for (int i = 0; i < NUM_MONTH; i++) {
                mMonthDateModels.set(i, monthDateModels.get(i));
            }
            lv_month.setSelection(INIT_POSITION);
        } else {
            ((MainActivity)mActivity).setFragment(0);
        }
    }
}
