package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventRepeatAdapter;
import com.timemanagement.zxg.adapter.GroupListAdapter;
import com.timemanagement.zxg.adapter.GroupRepeatAdapter;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.UIUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zxg on 17/2/7.
 */

public class EventListFragment extends Fragment {

    private Activity mActivity;
    private TextView tv_day, tv_week, tv_weekday, tv_month, tv_year;
    private RecyclerView rv_event_days, rv_event_weeks,
            rv_event_weekdays, rv_event_months, rv_event_years;

    private LinearLayoutManager recyclerLayoutManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        initView();
        initData();
    }

    private void initView(){
        tv_day = (TextView) mActivity.findViewById(R.id.tv_day);
        tv_week = (TextView) mActivity.findViewById(R.id.tv_week);
        tv_weekday = (TextView) mActivity.findViewById(R.id.tv_weekday);
        tv_month = (TextView) mActivity.findViewById(R.id.tv_month);
        tv_year = (TextView) mActivity.findViewById(R.id.tv_year);

        rv_event_days = (RecyclerView) mActivity.findViewById(R.id.rv_event_days);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_days.setHasFixedSize(true);
        recyclerLayoutManagement = new LinearLayoutManager(mActivity);
        rv_event_days.setLayoutManager(recyclerLayoutManagement);
        rv_event_weekdays = (RecyclerView) mActivity.findViewById(R.id.rv_event_weekdays);
        rv_event_weeks = (RecyclerView) mActivity.findViewById(R.id.rv_event_weeks);
        rv_event_months = (RecyclerView) mActivity.findViewById(R.id.rv_event_months);
        rv_event_years = (RecyclerView) mActivity.findViewById(R.id.rv_event_years);

        ((BaseActivity)mActivity).tv_left.setVisibility(View.GONE);
    }

    private void initData(){
        Map<String, List<EventModel>> maps = new DatabaseUtil(mActivity).queryByRepeatType();

        setAdapters(maps.get("1"), tv_day, rv_event_days);
//        UIUtils.setListViewHeightBasedOnChildren(lv_event_days);
        setAdapters(maps.get("2"), tv_weekday, rv_event_weekdays);
//        UIUtils.setListViewHeightBasedOnChildren(lv_event_weekdays);
        setAdapters(maps.get("3"), tv_week, rv_event_weeks);
//        UIUtils.setListViewHeightBasedOnChildren(lv_event_weeks);
        setAdapters(maps.get("4"), tv_month, rv_event_months);
//        UIUtils.setListViewHeightBasedOnChildren(lv_event_months);
        setAdapters(maps.get("5"), tv_year, rv_event_years);
//        UIUtils.setListViewHeightBasedOnChildren(lv_event_years);
    }

    private void setAdapters(List<EventModel> eventModels, TextView textView, RecyclerView rv_event) {
        if (eventModels != null && eventModels.size() > 0) {
            rv_event.setAdapter(new EventRepeatAdapter(mActivity, eventModels));
            rv_event.setVisibility(View.VISIBLE);
        } else {
            rv_event.setVisibility(View.GONE);
        }
    }
}
