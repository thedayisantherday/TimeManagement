package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.ActivityManager;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventYearAdapter;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventYearActivity extends BaseActivity  implements View.OnClickListener {

    private TextView tv_today, tv_repeat/*, tv_out_date*/;

    private RecyclerView rv_year;
    private LinearLayoutManager recyclerLayoutManagement = new LinearLayoutManager(mthis);
    private List<YearDateModel> mYearDateModels;
    private EventYearAdapter eventYearAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;
    private boolean isCurrent = true;

    private final static int NUM_YEAR = 5;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_YEAR+(NUM_YEAR-1)/2;
    private int frontPoint = INIT_POSITION;

    private int offset;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_year);

        initView();
        initData();
    }

    private void initView(){
        rv_year = (RecyclerView) findViewById(R.id.rv_year);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_year.setHasFixedSize(true);
        rv_year.setLayoutManager(recyclerLayoutManagement);

        tv_today = (TextView)findViewById(R.id.tv_today);
        tv_today.setOnClickListener(this);
        tv_repeat = (TextView)findViewById(R.id.tv_repeat);
        tv_repeat.setOnClickListener(this);
//        tv_out_date = (TextView)findViewById(tv_out_date);
//        tv_out_date.setOnClickListener(this);
    }

    private void initData(){
        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", mCalendar.get(Calendar.YEAR));
        if (mYear<=0){
            mYear = mCalendar.get(Calendar.YEAR);
        }
        offset = mYear-mCalendar.get(Calendar.YEAR);
        mYearDateModels = getYearList(mYear);
        eventYearAdapter = new EventYearAdapter(mContext, mYearDateModels);
        eventYearAdapter = new EventYearAdapter(mContext, mYearDateModels);
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

    @Override
    public void initHead(ViewHolder viewHolder) {
        viewHolder.iv_left.setImageResource(R.drawable.icon_share);
        viewHolder.iv_left.setVisibility(View.GONE);
        viewHolder.iv_right.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setOnClickListener(this);
//        viewHolder.iv_right2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_right:
                EventEditActivity.startSelf(mContext, 0, null);
                break;
            case R.id.tv_today:
                gotoToday();
                break;
            case R.id.tv_repeat:
                EventRepeatActivity.startSelf(mContext);
//            case tv_out_date:
//                ActivityManager activityManager = ActivityManager.getInstance();
//                for (int i = 0; i < 2; i++) {
//                    activityManager.finishActivity(activityManager.getTopActivity());
//                }
//                EventListActivity.startSelf(mContext);
                break;
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
            EventMonthActivity.startSelf(mContext,
                    mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
            ActivityManager.getInstance().finishActivity(mthis);
        }
    }

    public static void startSelf(Context context, int year){
        Intent intent = new Intent(context, EventYearActivity.class);
        intent.putExtra("year", year);
        context.startActivity(intent);
    }
}
