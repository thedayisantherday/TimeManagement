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
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
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
    private List<MonthDateModel> mMonthDateModels;
    private EventMonthAdapter0 eventMonthAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isFirst = true;

    private boolean isCurrent = true;

    private final static int NUM_MONTH = 12;
    private final static int INIT_POSITION = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%NUM_MONTH+(NUM_MONTH-1)/2;

    private int frontPoint = INIT_POSITION;
    private int offset;
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

    @Override
    public void onStart() {
        super.onStart();
//        gotoToday();
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
        if (mYear<=0){
            mYear = mCalendar.get(Calendar.YEAR);
        }
        if (mMonth<=0){
            mMonth = mCalendar.get(Calendar.MONTH)+1;
        }

        if ((mMonth != mCalendar.get(Calendar.MONTH)+1) || (mYear != mCalendar.get(Calendar.YEAR))){
            isCurrent = false;
        }

        offset =  (mYear-mCalendar.get(Calendar.YEAR))*12+mMonth-(mCalendar.get(Calendar.MONTH)+1);
        LogUtils.i("EventMonthActivity", "mYear:"+mYear+", mMonth:"+mMonth);
        mMonthDateModels = getList(mYear, mMonth);
        eventMonthAdapter = new EventMonthAdapter0(mActivity, mMonthDateModels);
        rv_month.setAdapter(eventMonthAdapter);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_month.setHasFixedSize(true);
        recyclerLayoutManagement = new LinearLayoutManager(mActivity);
        rv_month.setLayoutManager(recyclerLayoutManagement);
        rv_month.scrollToPosition(INIT_POSITION);
        rv_month.setOnScrollListener(new EventMonthRecyclerScrollListerer());
    }

    /**
     * 获取数据
     * @return
     */
    public List<MonthDateModel> getList(int year, int month){
        List<MonthDateModel> list =  new ArrayList<MonthDateModel>();
        if (year>0 && month>0 && month<=12){
            for (int i = 0; i < NUM_MONTH; i++) {
                if (month-(NUM_MONTH-1)/2+i<=0){
                    if (year<=1){
                        continue;
                    }
                    list.add(DateModelUtil.getMonthDateModel(year-1, month-(NUM_MONTH-1)/2+i+12));
                } else if (month-(NUM_MONTH-1)/2+i<=12){
                    list.add(DateModelUtil.getMonthDateModel(year, month-(NUM_MONTH-1)/2+i));
                } else {
                    list.add(DateModelUtil.getMonthDateModel(year+1, month-(NUM_MONTH-1)/2+i-12));
                }
            }
        }
        return list;
    }

    public void gotoToday(){
        if (!isCurrent) {
            List<MonthDateModel> monthDateModels = getList(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH)+1);
            for (int i = 0; i < NUM_MONTH; i++) {
                mMonthDateModels.set(i, monthDateModels.get(i));
            }
            rv_month.scrollToPosition(INIT_POSITION);
        } else {
            ((MainActivity)mActivity).setFragment(0, null);
        }
    }

    class EventMonthRecyclerScrollListerer extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            switch (scrollState){
                case 0:
                    ((MainActivity)mActivity).setTopLefText(eventMonthAdapter.getItem(frontPoint).getYear() + "年", View.VISIBLE);
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

            if (!isFirst) {
                int firstVisibleItem = recyclerLayoutManagement.findFirstVisibleItemPosition();
                LogUtils.i(TAG, "frontPoint:"+frontPoint+",firstVisibleItem:" + firstVisibleItem);

                if (firstVisibleItem != frontPoint) {
                    tv_month.setText(eventMonthAdapter.getItem(firstVisibleItem).getYear() + "年"
                            + eventMonthAdapter.getItem(firstVisibleItem).getMonth() + "月");
                }
                int num;
                int init_year = mCalendar.get(Calendar.YEAR);
                int init_month = mCalendar.get(Calendar.MONTH)+1;
                int num_year = (firstVisibleItem - INIT_POSITION + offset)/12;
                int num_month = (firstVisibleItem - INIT_POSITION + offset)%12;
                LogUtils.i("num", "offset:"+offset+",INIT_POSITION:"+INIT_POSITION+",firstVisibleItem:"+firstVisibleItem);

                if (firstVisibleItem < frontPoint) {
                    int[] start_month = DateModelUtil.getStandardMonth(
                            init_year + num_year, init_month + num_month-(NUM_MONTH-1)/2);
                    MonthDateModel model = DateModelUtil.getMonthDateModel(start_month[0], start_month[1]);

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
                    MonthDateModel model = DateModelUtil.getMonthDateModel(start_month[0], start_month[1]);

                    num = (firstVisibleItem - INIT_POSITION - (NUM_MONTH-(NUM_MONTH-1)/2*2)) % NUM_MONTH;
                    if (num < 0){
                        num = num + NUM_MONTH;
                    }
                    mMonthDateModels.set(num, model);
                    LogUtils.i("num1", num+"");
                }
                mYear = Integer.valueOf(eventMonthAdapter.getItem(firstVisibleItem).getYear());
                mMonth = Integer.valueOf(eventMonthAdapter.getItem(firstVisibleItem).getMonth());
                if ((mMonth == mCalendar.get(Calendar.MONTH)+1) && (mYear == mCalendar.get(Calendar.YEAR))){
                    isCurrent = true;
                } else {
                    isCurrent = false;
                }
                frontPoint = firstVisibleItem;
            }else {
                LogUtils.i("mMonthDateModels", mMonthDateModels.toString());
                ((MainActivity)mActivity).setTopLefText(eventMonthAdapter.getItem(INIT_POSITION).getYear() + "年", View.VISIBLE);
                isFirst = false;
            }
        }
    }
}
