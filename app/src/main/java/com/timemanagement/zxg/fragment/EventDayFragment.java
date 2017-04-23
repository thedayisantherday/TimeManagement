package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.timemanagement.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDayFragment extends Fragment {

    private Activity mActivity;
    private ViewPager vp_date;
    private TextView tv_date_detail;

    private List<View> vp_views = new ArrayList<View>();

    private int mPosition = 1; //当前banner的位置

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_day, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();

        initView();
        initData();
    }

    private void initView(){
        vp_date = (ViewPager) mActivity.findViewById(R.id.vp_date);
        tv_date_detail = (TextView) mActivity.findViewById(R.id.tv_date_detail);

        for (int i = 0; i < 5; i++) {
            vp_views.add(View.inflate(mActivity, R.layout.viewpager_item_date, null));
        }
    }

    private void initData(){

        Calendar calendar = Calendar.getInstance();
        ((BaseActivity)mActivity).tv_left.setVisibility(View.VISIBLE);
        ((BaseActivity)mActivity).tv_left.setText(
                calendar.get(Calendar.YEAR)+"年"+calendar.get(Calendar.MONTH)+1+"月");

//        vp_date.setAdapter(new ViewPagerAdapter(vp_views));
        vp_date.setOffscreenPageLimit(1);
        vp_date.setCurrentItem(1);
        vp_date.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //ViewPager跳转
                int pageIndex = mPosition;
                if(mPosition == 0){
                    pageIndex = vp_views.size()-2;
                }else if(mPosition == vp_views.size()-1){
                    pageIndex = 1;
                }
                if (pageIndex != mPosition) {
                    vp_date.setCurrentItem(pageIndex, false);
                }
            }
        });

    }
}
