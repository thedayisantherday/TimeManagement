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

    private TextView tv_today, tv_repeat, tv_out_date;

    private EventDayFragment eventDayFragment;
    private EventMonthFragment eventMonthFragment;
    private EventYearFragment eventYearFragment;
    private EventListFragment eventListFragment;

    //fragment类型，1:EventDayFragment，2:EventListFragment，
    // 0:EventMonthFragment，－1:eventYearFragment
    private int type_fragment = 1000;
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
        tv_out_date = (TextView) findViewById(R.id.tv_out_date);
        tv_out_date.setOnClickListener(this);
    }

    private void initData(){
        eventDayFragment = new EventDayFragment();
        eventMonthFragment = new EventMonthFragment();
        eventYearFragment = new EventYearFragment();
        eventListFragment = new EventListFragment();

        setFragment(TYPE_DAY, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_today:
                switch (type_fragment) {
                    case TYPE_OUTDAY:
                    case TYPE_REPEAT:
                        setFragment(TYPE_DAY, null);
                        break;
                    case TYPE_DAY:
                        eventDayFragment.gotoToday();
                        break;
                    case TYPE_MONTH:
                        eventMonthFragment.gotoToday();
                        break;
                    case TYPE_YEAR:
                        eventYearFragment.gotoToday();
                        break;
                }
                break;
            case R.id.tv_repeat:
                setFragment(TYPE_REPEAT, null);
                break;
            case R.id.tv_out_date:
                setFragment(TYPE_OUTDAY, null);
                break;
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
                        setFragment(TYPE_YEAR, null);
                        break;
                    case 0:
                        setFragment(TYPE_MONTH, null);
                        break;
                }
        }
    }

    private void setTab(int type){
        switch (type){
            case TYPE_DAY:
                tv_today.setTextColor(getResources().getColor(R.color.red));
                tv_repeat.setTextColor(getResources().getColor(R.color.black));
                tv_out_date.setTextColor(getResources().getColor(R.color.black));
                break;
            case TYPE_REPEAT:
                tv_repeat.setTextColor(getResources().getColor(R.color.red));
                tv_today.setTextColor(getResources().getColor(R.color.black));
                tv_out_date.setTextColor(getResources().getColor(R.color.black));
                break;
            case TYPE_OUTDAY:
                tv_out_date.setTextColor(getResources().getColor(R.color.red));
                tv_repeat.setTextColor(getResources().getColor(R.color.black));
                tv_today.setTextColor(getResources().getColor(R.color.black));
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

    public void setTopLefText (String topLefText, int visible) {
        if (mViewHolder.tv_left.getVisibility() != visible) {
            mViewHolder.tv_left.setVisibility(visible);
        }
        mViewHolder.tv_left.setText(topLefText);
    }

    public void setFragment(int type, Bundle argBundle){
        if (type_fragment == type) {
            return;
        }

        type_fragment = type;
        setTab(type);

        switch (type){
            case TYPE_YEAR:
                eventYearFragment.setArguments(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventYearFragment).commit(); // 是异步操作
                mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
            case TYPE_MONTH:
                eventMonthFragment.setArguments(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventMonthFragment).commit();
                mViewHolder.iv_left.setImageResource(R.drawable.head_left);
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
            case TYPE_DAY:
                eventDayFragment.setArguments(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventDayFragment).commit();
                mViewHolder.iv_left.setImageResource(R.drawable.head_left);
                mViewHolder.iv_right1.setVisibility(View.VISIBLE);
                break;
            case TYPE_OUTDAY:
            case TYPE_REPEAT:
//                eventListFragment.setArguments(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventListFragment).commit();
                mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
        }
    }
}