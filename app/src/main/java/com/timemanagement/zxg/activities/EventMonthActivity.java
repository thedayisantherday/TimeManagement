package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.ActivityManager;
import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.EventMonthAdapter;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DateModelUtil;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventMonthActivity extends BaseActivity  implements AbsListView.OnScrollListener, View.OnClickListener {

    private TextView tv_today, tv_repeat/*, tv_out_date*/;
    private ListView lv_month, lv_event_list;
    private TextView tv_month;
    private View view_line;

    private List<MonthDateModel> mMonthDateModels;
    private EventMonthAdapter eventMonthAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;
    private boolean isCurrent = true;

    private final static int NUM_MONTH = 12;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_MONTH+(NUM_MONTH-1)/2;
    private int frontPoint = INIT_POSITION;

    private int offset;

    private int mYear, mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_month);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 解决getIntent()取到的intent为旧的intent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initView() {
        tv_month = (TextView) findViewById(R.id.tv_month);
        lv_month = (ListView) findViewById(R.id.lv_month);
        lv_event_list = (ListView) findViewById(R.id.lv_event_list);
        view_line = findViewById(R.id.view_line);

        tv_today = (TextView)findViewById(R.id.tv_today);
        tv_today.setOnClickListener(this);
        tv_repeat = (TextView)findViewById(R.id.tv_repeat);
        tv_repeat.setOnClickListener(this);
//        tv_out_date = (TextView)findViewById(tv_out_date);
//        tv_out_date.setOnClickListener(this);
    }

    private void initData(){
        Calendar calendar = Calendar.getInstance();
        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", calendar.get(Calendar.YEAR));
        if (mYear<=0){
            mYear = calendar.get(Calendar.YEAR);
        }
        mMonth = intent.getIntExtra("month", calendar.get(Calendar.MONTH)+1);
        if (mMonth<=0){
            mMonth = calendar.get(Calendar.MONTH)+1;
        }

        mViewHolder.tv_left.setVisibility(View.VISIBLE);
        mViewHolder.tv_left.setText(mYear+"年");

        offset =  (mYear-mCalendar.get(Calendar.YEAR))*12+mMonth-(mCalendar.get(Calendar.MONTH)+1);
        LogUtils.i("EventMonthActivity", "mYear:"+mYear+", mMonth:"+mMonth);
        mMonthDateModels = getList(mYear, mMonth);
        eventMonthAdapter = new EventMonthAdapter(mContext, mMonthDateModels);
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
        if (year<0 || month<=0 || month>12){
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
        switch (scrollState){
            case 0:
                mViewHolder.tv_left.setText(eventMonthAdapter.getItem(frontPoint).getYear() + "年");
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

            if (firstVisibleItem != frontPoint) {
                tv_month.setText(eventMonthAdapter.getItem(firstVisibleItem).getYear() + "年"
                        + eventMonthAdapter.getItem(firstVisibleItem).getMonth() + "月");
            }
            int num;
            int init_year = mCalendar.get(Calendar.YEAR);
            int init_month = mCalendar.get(Calendar.MONTH)+1;
            int num_year = (firstVisibleItem - INIT_POSITION + offset)/12;
            int num_month = (firstVisibleItem - INIT_POSITION + offset)%12;
            LogUtils.i("num", "INIT_POSITION"+INIT_POSITION+",firstVisibleItem:"+firstVisibleItem);

            if (firstVisibleItem < frontPoint) {
                int[] start_month = DateModelUtil.getStandardMonth(
                        init_year + num_year, init_month + num_month-(NUM_MONTH-1)/2);
                MonthDateModel model = getMonthDateModel(start_month[0], start_month[1]);

                num = (firstVisibleItem - INIT_POSITION) % NUM_MONTH;
                if (num < 0){
                    num = num + NUM_MONTH;
                }
                mMonthDateModels.set(num, model);
                LogUtils.i("num0", num+"");
            }

            if (firstVisibleItem > frontPoint) {
                int[] start_month = DateModelUtil.getStandardMonth(
                        init_year + num_year, init_month + num_month+(NUM_MONTH-1)/2);
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
            mViewHolder.tv_left.setText(eventMonthAdapter.getItem(INIT_POSITION).getYear() + "年");
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
            tv_left.setText(mMonthDateModels.get(5).getYear()+"年");
        } else {
            EventDayActivity.startSelf(mContext);
            ActivityManager.getInstance().finishActivity(mthis);
        }
    }

    @Override
    public void initHead(ViewHolder viewHolder) {
        viewHolder.ll_left.setVisibility(View.VISIBLE);
        viewHolder.ll_left.setOnClickListener(this);
        viewHolder.iv_left.setImageResource(R.drawable.head_left);
        viewHolder.tv_left.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setOnClickListener(this);
//        viewHolder.iv_right1.setVisibility(View.VISIBLE);
        viewHolder.iv_right1.setOnClickListener(this);
//        viewHolder.iv_right2.setVisibility(View.VISIBLE);
    }

    public static void startSelf(Context context, int year, int month){
        Intent intent = new Intent(context, EventMonthActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_left:
                EventYearActivity.startSelf(mContext,
                        Integer.valueOf(eventMonthAdapter.getItem(frontPoint).getYear()));
                break;
            case R.id.iv_right:
                EventEditActivity.startSelf(mContext, 0, null);
                break;
            case R.id.iv_right1:
                if (view_line.getVisibility()==View.VISIBLE){
                    lv_event_list.setVisibility(View.GONE);
                    view_line.setVisibility(View.GONE);
                } else {
                    lv_event_list.setVisibility(View.VISIBLE);
                    view_line.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_today:
                gotoToday();
                break;
            case R.id.tv_repeat:
//            case tv_out_date:
                EventListActivity.startSelf(mContext);
                ActivityManager.getInstance().finishActivity(mthis);
                break;

        }
    }
}
