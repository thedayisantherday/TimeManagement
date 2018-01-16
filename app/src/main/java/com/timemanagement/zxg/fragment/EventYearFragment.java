package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timemanagement.zxg.activities.EventYearActivity;
import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.adapter.EventYearAdapter;
import com.timemanagement.zxg.adapter.EventYearAdapter1;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/2/22.
 */

public class EventYearFragment extends Fragment {

    private Activity mActivity;
    private RecyclerView rv_year;
    private LinearLayoutManager recyclerLayoutManagement;
    private List<YearDateModel> mYearDateModels;
    private EventYearAdapter1 eventYearAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;
    private boolean isCurrent = true;

    private final static int NUM_YEAR = 7; // 当NUM_YEAR=5时，必须保证一屏内只有一个item，否则数据会乱；若一屏内有m个item，则NUM_YEAR=5+(m-1)*2;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_YEAR+(NUM_YEAR-1)/2;
    private int frontPoint = INIT_POSITION;

    private int offset;
    private int mYear;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_year, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();
        recyclerLayoutManagement = new LinearLayoutManager(mActivity);
        rv_year = (RecyclerView) mActivity.findViewById(R.id.rv_year);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_year.setHasFixedSize(true);
        rv_year.setLayoutManager(recyclerLayoutManagement);
        ((MainActivity)mActivity).setTopLefText("", View.GONE);

        initData();
    }

    private void initData(){
        Bundle arguments = getArguments();
        if (arguments != null) {
            mYear = arguments.getInt("year", mCalendar.get(Calendar.YEAR));
        }
        if (mYear<=0){
            mYear = mCalendar.get(Calendar.YEAR);
        }
        offset = mYear-mCalendar.get(Calendar.YEAR);
        mYearDateModels = getYearList(mYear);
//        eventYearAdapter = new EventYearAdapter(mActivity, mYearDateModels);
        eventYearAdapter = new EventYearAdapter1(mActivity, mYearDateModels);
        rv_year.setAdapter(eventYearAdapter);
        rv_year.scrollToPosition(INIT_POSITION);
        rv_year.setOnScrollListener(new RecyclerViewOnScrollListener());
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
                list.add(DateModelUtil.getMonthDateModel(year, i, false));
            }
        }
        return list;
    }

    class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = recyclerLayoutManagement.findFirstVisibleItemPosition();
            if (firstVisibleItem == INIT_POSITION){
                isCurrent = true;
            }else {
                isCurrent = false;
            }

            if (!isFirst) {
                int num;
                int init_year = mCalendar.get(Calendar.YEAR);
                int num_year = firstVisibleItem - INIT_POSITION + offset;
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

                    num = (num_year - offset - (NUM_YEAR-(NUM_YEAR-1)/2*2)) % NUM_YEAR;
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
    }

    public void gotoToday(){
        if (!isCurrent) {
            List<YearDateModel> yearDateModels = getYearList(mCalendar.get(Calendar.YEAR));
            for (int i = 0; i < NUM_YEAR; i++) {
                mYearDateModels.set(i, yearDateModels.get(i));
            }
            rv_year.scrollToPosition(INIT_POSITION);
        } else {
            ((MainActivity)mActivity).setFragment(-1, null);
        }
    }
}
