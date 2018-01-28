package com.timemanagement.zxg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.fragment.EventDayFragment;
import com.timemanagement.zxg.fragment.EventListFragment;
import com.timemanagement.zxg.fragment.EventMonthFragment;
import com.timemanagement.zxg.fragment.EventYearFragment;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_today, tv_repeat, tv_out_date;

    public static EventDayFragment eventDayFragment;
    private EventMonthFragment eventMonthFragment;
    private EventYearFragment eventYearFragment;
    private EventListFragment eventListFragment;

    //fragment类型，1:EventDayFragment，2:EventListFragment，
    // 0:EventMonthFragment，-1:eventYearFragment
    public static int REQUEST_CODE = 8;
    private final int TYPE_YEAR = -2;
    private final int TYPE_MONTH = -1;
    private final int TYPE_DAY = 0;
    private final int TYPE_REPEAT = 1;
    private final int TYPE_OUTDAY = 2;
    private int type_fragment = 1000;
    private int pre_type_fragment = TYPE_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_main);

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
                        setFragment(pre_type_fragment, null);
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
                EventEditActivity.startSelf(mthis, 0, null);
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
                        setFragment(TYPE_YEAR, eventMonthFragment.getArgBundle());
                        break;
                    case 0:
                        setFragment(TYPE_MONTH, eventDayFragment.getArgBundle());
                        break;
                }
        }
    }

    private void setTab(int type){
        switch (type){
            case TYPE_DAY:
            case TYPE_MONTH:
            case TYPE_YEAR:
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
        } else if (type_fragment != TYPE_OUTDAY && type_fragment != TYPE_REPEAT) {
            pre_type_fragment = type_fragment;
        }

        type_fragment = type;
        setTab(type);

        switch (type){
            case TYPE_YEAR:
                eventYearFragment.setArgumentBundle(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventYearFragment).commit(); // 是异步操作
                mViewHolder.iv_left.setImageResource(R.drawable.icon_share);
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
            case TYPE_MONTH:
                eventMonthFragment.setArgumentBundle(argBundle);
                getFragmentManager().beginTransaction().replace(R.id.fl_content, eventMonthFragment).commit();
                mViewHolder.iv_left.setImageResource(R.drawable.head_left);
                mViewHolder.iv_right1.setVisibility(View.GONE);
                break;
            case TYPE_DAY:
                eventDayFragment.setArgumentBundle(argBundle);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        type_fragment = 1000;
        if (resultCode == EventEditActivity.RESULT_CODE && requestCode == REQUEST_CODE) {
            Bundle arguments = data.getBundleExtra("arguments");
            setFragment(TYPE_DAY, arguments);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(mContext, "退出App中...", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}