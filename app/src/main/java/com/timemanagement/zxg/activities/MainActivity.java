package com.timemanagement.zxg.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.fragment.EventDayFragment;
import com.timemanagement.zxg.fragment.EventListFragment;
import com.timemanagement.zxg.fragment.EventMonthFragment;
import com.timemanagement.zxg.fragment.EventYearFragment;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_today, tv_repeat/*, tv_out_date*/;

    private EventDayFragment eventDayFragment;
    private EventMonthFragment eventMonthFragment;
    private EventYearFragment eventYearFragment;
    private EventListFragment eventListFragment;

    //fragment类型，1:EventDayFragment，2:EventListFragment，
    // 0:EventMonthFragment，－1:eventYearFragment
    private int type_fragment;
    private final int TYPE_YEAR = -2;
    private final int TYPE_MONTH = -1;
    private final int TYPE_DAY = 0;
    private final int TYPE_REPEAT = 1;
    private final int TYPE_OUTDAY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_main);

        /*Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String d = dateFormat.format(date);
        Calendar today = Calendar.getInstance();
        try {
            today.setTime(chineseDateFormat.parse(d));
        }catch (Exception e){
            e.printStackTrace();
        }
        LunarUtils lunar = new LunarUtils(today);
        System.out.println("北京时间：" + chineseDateFormat.format(today.getTime())
                + "　农历" + lunar);*/

//        LunarUtils lunarUtils = new LunarUtils();
//        lunarUtils.getLunar();

        initView();
        initData();
    }

    private void initView(){
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_today.setOnClickListener(this);
        tv_repeat = (TextView) findViewById(R.id.tv_repeat);
        tv_repeat.setOnClickListener(this);
//        tv_out_date = (TextView) findViewById(R.id.tv_out_date);
//        tv_out_date.setOnClickListener(this);
    }

    private void initData(){
        type_fragment = 0;

        eventDayFragment = new EventDayFragment();
        getFragmentManager().beginTransaction().replace(R.id.fl_content, eventDayFragment).commit();

        eventMonthFragment = new EventMonthFragment();
        eventYearFragment = new EventYearFragment();
        eventListFragment = new EventListFragment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_today:
                setTab(0);
                switch (type_fragment) {
                    case 0:
                        mViewHolder.iv_right1.setVisibility(View.VISIBLE);
                        getFragmentManager().beginTransaction().replace(R.id.fl_content, eventDayFragment).commit();
                        break;
                    case -1:
                        eventMonthFragment.gotoToday();
                        break;
                    case -2:
                        eventYearFragment.gotoToday();
                        break;
                }
                break;
            case R.id.tv_repeat:
                mViewHolder.iv_right1.setVisibility(View.GONE);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventListFragment).commit();
                setTab(1);
                type_fragment = 1;
                break;
//            case R.id.tv_out_date:
//                mViewHolder.iv_right1.setVisibility(View.GONE);
//                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventListFragment).commit();
//                setTab(2);
//                type_fragment = 2;
//                break;
            case R.id.iv_right:
                EventEditActivity.startSelf(mContext, 0, null);
                break;
            case R.id.ll_left:
                switch (type_fragment){
                    case -2:
                    case 1:
                    case 2:
                        //TODO 分享
                        LogUtils.i("ll_left onClick", "share btn is onClick");
                        break;
                    case -1:
                        getFragmentManager().beginTransaction().replace(R.id.fl_content, eventYearFragment).commit();
                        mViewHolder.iv_right1.setVisibility(View.GONE);
                        mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                        break;
                    case 0:
                        getFragmentManager().beginTransaction().replace(R.id.fl_content, eventMonthFragment).commit();
                        mViewHolder.iv_right1.setVisibility(View.GONE);
                        break;
                }
                if (type_fragment==-1 || type_fragment==0){
                    type_fragment--;
                }
//                startActivity(new Intent(this, TestActivity.class));
        }
    }

    private void setTab(int num){
        switch (num){
            case 0:
                tv_today.setTextColor(getResources().getColor(R.color.red));
                tv_repeat.setTextColor(getResources().getColor(R.color.black));
//                tv_out_date.setTextColor(getResources().getColor(R.color.black));
                mViewHolder.iv_left.setImageResource(R.drawable.head_left);
                break;
            case 1:
                tv_repeat.setTextColor(getResources().getColor(R.color.red));
                tv_today.setTextColor(getResources().getColor(R.color.black));
//                tv_out_date.setTextColor(getResources().getColor(R.color.black));
                mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                break;
            case 2:
//                tv_out_date.setTextColor(getResources().getColor(R.color.red));
                tv_repeat.setTextColor(getResources().getColor(R.color.black));
                tv_today.setTextColor(getResources().getColor(R.color.black));
                mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                break;
        }
    }

    @Override
    public void initHead(ViewHolder viewHolder) {
        mViewHolder = viewHolder;
        viewHolder.ll_left.setOnClickListener(this);
        viewHolder.iv_right.setVisibility(View.VISIBLE);
        viewHolder.iv_right.setImageResource(R.drawable.head_plus);
        viewHolder.iv_right.setOnClickListener(this);
        viewHolder.iv_right1.setVisibility(View.VISIBLE);
        viewHolder.iv_right1.setOnClickListener(this);
        viewHolder.iv_right2.setVisibility(View.VISIBLE);
        viewHolder.iv_right2.setOnClickListener(this);
    }

    public void setFragment(int type){
        type_fragment = type;
        switch (type){
            case -2:
                break;
            case -1:
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventMonthFragment).commit();
                eventMonthFragment.gotoToday();
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
            case 0:
                mViewHolder.iv_right1.setVisibility(View.VISIBLE);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventDayFragment).commit();
                break;
        }
    }
}