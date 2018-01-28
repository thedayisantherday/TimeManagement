package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class EventListFragment extends Fragment implements View.OnClickListener{

    private Activity mActivity;
    private TextView tv_day, tv_week, tv_weekday, tv_month, tv_year;
    private ImageView iv_day, iv_week, iv_weekday, iv_month, iv_year;
    private RecyclerView rv_event_days, rv_event_weeks,
            rv_event_weekdays, rv_event_months, rv_event_years;

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

        iv_day = (ImageView) mActivity.findViewById(R.id.iv_day);
        iv_day.setOnClickListener(this);
        iv_week = (ImageView) mActivity.findViewById(R.id.iv_week);
        iv_week.setOnClickListener(this);
        iv_weekday = (ImageView) mActivity.findViewById(R.id.iv_weekday);
        iv_weekday.setOnClickListener(this);
        iv_month = (ImageView) mActivity.findViewById(R.id.iv_month);
        iv_month.setOnClickListener(this);
        iv_year = (ImageView) mActivity.findViewById(R.id.iv_year);
        iv_year.setOnClickListener(this);

        rv_event_days = (RecyclerView) mActivity.findViewById(R.id.rv_event_days);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_days.setHasFixedSize(true);
        LinearLayoutManager rlm_event_days = new LinearLayoutManager(mActivity);
        rv_event_days.setLayoutManager(rlm_event_days);

        rv_event_weekdays = (RecyclerView) mActivity.findViewById(R.id.rv_event_weekdays);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_weekdays.setHasFixedSize(true);
        LinearLayoutManager rlm_event_weekdays = new LinearLayoutManager(mActivity);
        rv_event_weekdays.setLayoutManager(rlm_event_weekdays);

        rv_event_weeks = (RecyclerView) mActivity.findViewById(R.id.rv_event_weeks);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_weeks.setHasFixedSize(true);
        LinearLayoutManager rlm_event_weeks = new LinearLayoutManager(mActivity);
        rv_event_weeks.setLayoutManager(rlm_event_weeks);

        rv_event_months = (RecyclerView) mActivity.findViewById(R.id.rv_event_months);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_months.setHasFixedSize(true);
        LinearLayoutManager rlm_event_months = new LinearLayoutManager(mActivity);
        rv_event_months.setLayoutManager(rlm_event_months);

        rv_event_years = (RecyclerView) mActivity.findViewById(R.id.rv_event_years);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_event_years.setHasFixedSize(true);
        LinearLayoutManager rlm_event_years = new LinearLayoutManager(mActivity);
        rv_event_years.setLayoutManager(rlm_event_years);

        ((BaseActivity)mActivity).tv_left.setVisibility(View.GONE);
    }

    private void initData(){
        Map<String, List<EventModel>> maps = new DatabaseUtil(mActivity).queryByRepeatType();

        setAdapters(maps.get("1"), tv_day, rv_event_days);
        setAdapters(maps.get("2"), tv_weekday, rv_event_weekdays);
        setAdapters(maps.get("3"), tv_week, rv_event_weeks);
        setAdapters(maps.get("4"), tv_month, rv_event_months);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_year:
                doUnfold(rv_event_years, iv_year);
                break;
            case R.id.iv_month:
                doUnfold(rv_event_months, iv_month);
                break;
            case R.id.iv_week:
                doUnfold(rv_event_weeks, iv_week);
                break;
            case R.id.iv_weekday:
                doUnfold(rv_event_weekdays, iv_weekday);
                break;
            case R.id.iv_day:
                doUnfold(rv_event_days, iv_day);
                break;
        }
    }

    private void doUnfold (RecyclerView recyclerView, ImageView imageView) {
        Drawable drawable;

        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            drawable = getResources().getDrawable(R.drawable.arrow_down);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            drawable = getResources().getDrawable(R.drawable.arrow_up);
        }
        imageView.setImageDrawable(drawable);
    }
}
