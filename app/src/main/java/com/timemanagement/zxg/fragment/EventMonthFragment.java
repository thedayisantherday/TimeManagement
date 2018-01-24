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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventMonthActivity;
import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventMonthAdapter;
import com.timemanagement.zxg.adapter.EventMonthAdapter0;
import com.timemanagement.zxg.adapter.EventMonthAdapter1;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 17/2/9.
 */

public class EventMonthFragment extends Fragment {

    private static String TAG = EventMonthFragment.class.getSimpleName();

    private Activity mActivity;
    private ListView lv_event_list;
    private RecyclerView rv_month;
    private LinearLayoutManager recyclerLayoutManagement;
    private TextView tv_month;
    private View view_line;

    private EventMonthAdapter1 eventMonthAdapter;
    private Calendar mCalendar = Calendar.getInstance();
    private boolean isCurrent = true;
    private int frontPoint;
    private int mYear, mMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_month, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        initView();
        initData();
    }

    private void initView() {
        tv_month = (TextView) mActivity.findViewById(R.id.tv_month);
        rv_month = (RecyclerView) mActivity.findViewById(R.id.rv_month);
        lv_event_list = (ListView) mActivity.findViewById(R.id.lv_event_list);
        view_line = mActivity.findViewById(R.id.view_line);
    }

    private void initData(){
        Bundle arguments = getArguments();
        if (arguments != null) {
            mYear = arguments.getInt("year", mCalendar.get(Calendar.YEAR));
            mMonth = arguments.getInt("month", mCalendar.get(Calendar.MONTH)+1);
        }
        if (mYear<=0){
            mYear = mCalendar.get(Calendar.YEAR);
        }
        if (mMonth<=0){
            mMonth = mCalendar.get(Calendar.MONTH)+1;
        }

        if ((mMonth != mCalendar.get(Calendar.MONTH)+1) || (mYear != mCalendar.get(Calendar.YEAR))){
            isCurrent = false;
        }

        /*mViewHolder.tv_left.setVisibility(View.VISIBLE);*/
        ((MainActivity)mActivity).setTopLefText(mYear+"年", View.VISIBLE);

        LogUtils.i("EventMonthActivity", "mYear:"+mYear+", mMonth:"+mMonth);
        eventMonthAdapter = new EventMonthAdapter1(mActivity);
        rv_month.setAdapter(eventMonthAdapter);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_month.setHasFixedSize(true);
        recyclerLayoutManagement = new LinearLayoutManager(mActivity);
        rv_month.setLayoutManager(recyclerLayoutManagement);
        rv_month.setOnScrollListener(new EventMonthRecyclerScrollListerer());
        frontPoint = (mYear-1)*12 + mMonth-1;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 放在initData()方法中无效，还是之前的position
        rv_month.scrollToPosition(frontPoint);
    }

    public void gotoToday(){
        if (!isCurrent) {
            rv_month.scrollToPosition((mCalendar.get(Calendar.YEAR)-1)*12 + mCalendar.get(Calendar.MONTH));
        } else {
            ((MainActivity)mActivity).setFragment(0, null);
        }
    }

    class EventMonthRecyclerScrollListerer extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            switch (scrollState){
                case 0:
                    ((MainActivity)mActivity).setTopLefText(frontPoint/12+1 + "年", View.VISIBLE);
                    tv_month.setVisibility(View.GONE);
                    break;
                default:
                    tv_month.setVisibility(View.VISIBLE);
                    break;
            }
            LogUtils.i("onScrollStateChanged", scrollState+"");
        }

        @Override
        public void onScrolled(RecyclerView view, int dx, int dy) {

            int firstVisibleItem = recyclerLayoutManagement.findFirstVisibleItemPosition();

            mYear = firstVisibleItem/12+1;
            mMonth = firstVisibleItem%12+1;
            isCurrent = isCurrentData(mYear, mMonth);
            if (firstVisibleItem != frontPoint) {
                tv_month.setText(mYear + "年" + mMonth + "月");
            }
            ((MainActivity)mActivity).setTopLefText(mYear + "年", View.VISIBLE);
            frontPoint = firstVisibleItem;
        }
    }

    private boolean isCurrentData (int year, int month) {
        boolean front = (month == mCalendar.get(Calendar.MONTH)+1) && (year == mCalendar.get(Calendar.YEAR));
        boolean behind = false;
        if (month >= 12) {
            month = 1;
            year = year + 1;
        }
        behind = (month == mCalendar.get(Calendar.MONTH)+1) && (year == mCalendar.get(Calendar.YEAR));
        return front || behind;
    }

    public Bundle getArgBundle () {
        Bundle arguments = new Bundle();
        arguments.putInt("year", mYear);
        return arguments;
    }
}
