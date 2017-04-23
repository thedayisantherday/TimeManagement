package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.GroupRepeatAdapter;
import com.timemanagement.zxg.database.DatabaseUtil;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventRepeatActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext;
    private TextView tv_day, tv_week, tv_weekday, tv_month, tv_year;
    private ListView lv_event_days, lv_event_weeks,
            lv_event_weekdays, lv_event_months, lv_event_years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_repeat);

        mContext = this;

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_week = (TextView) findViewById(R.id.tv_week);
        tv_weekday = (TextView) findViewById(R.id.tv_weekday);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_year = (TextView) findViewById(R.id.tv_year);

        lv_event_days = (ListView) findViewById(R.id.lv_event_days);
        lv_event_weekdays = (ListView) findViewById(R.id.lv_event_weekdays);
        lv_event_weeks = (ListView) findViewById(R.id.lv_event_weeks);
        lv_event_months = (ListView) findViewById(R.id.lv_event_months);
        lv_event_years = (ListView) findViewById(R.id.lv_event_years);
    }

    private void initData(){
        Map<String, List<EventModel>> maps = new DatabaseUtil(mContext).queryByRepeatType();

        setAdapters(maps.get("1"), tv_day, lv_event_days);
        UIUtils.setListViewHeightBasedOnChildren(lv_event_days);
        setAdapters(maps.get("2"), tv_weekday, lv_event_weekdays);
        UIUtils.setListViewHeightBasedOnChildren(lv_event_weekdays);
        setAdapters(maps.get("3"), tv_week, lv_event_weeks);
        UIUtils.setListViewHeightBasedOnChildren(lv_event_weeks);
        setAdapters(maps.get("4"), tv_month, lv_event_months);
        UIUtils.setListViewHeightBasedOnChildren(lv_event_months);
        setAdapters(maps.get("5"), tv_year, lv_event_years);
        UIUtils.setListViewHeightBasedOnChildren(lv_event_years);
    }

    private void setAdapters(List<EventModel> eventModels, TextView  textView, ListView lv_event) {
        if (eventModels != null && eventModels.size() > 0) {
            lv_event.setAdapter(new GroupRepeatAdapter(mContext, eventModels));
            textView.setVisibility(View.VISIBLE);
            lv_event.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
            lv_event.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_left:
                this.finish();
                break;
        }
    }

    @Override
    public void initHead(ViewHolder viewHolder) {
        viewHolder.ll_left.setVisibility(View.VISIBLE);
        viewHolder.ll_left.setOnClickListener(this);
        viewHolder.tv_left.setVisibility(View.VISIBLE);
        viewHolder.tv_left.setText("返回");
    }

    public static void startSelf(Context context){
        Intent intent = new Intent(context, EventRepeatActivity.class);
        context.startActivity(intent);
    }
}
