package com.timemanagement.zxg.widget.wheelview;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.widget.wheelview.adapters.ArrayWheelAdapter;
import com.timemanagement.zxg.widget.wheelview.view.WheelView;

import java.util.List;

/**
 * Created by zxg on 17/2/6.
 */

public class WheelContentSelector {

    private static final String TAG = WheelContentSelector.class.getSimpleName();

    private Context mContext;
    public Button btn_wheel_confirm, btn_wheel_cancel;
    public TextView tv_wheel_title;
    private View mView;
    private WheelView mWheelView, mWheelView1;

    private List<String> mContents;
    private List<String> mContents1;
    private String content;

    public WheelContentSelector(Context context, View view
            , List<String> contents, List<String> contents1) {
        this.mContext = context;
        this.mView = view;
        this.mContents = contents;
        this.mContents1 = contents1;

        initView();

        initData();
    }

    public WheelContentSelector(Context context, View view, List<String> contents) {
        this.mContext = context;
        this.mView = view;
        this.mContents = contents;

        initView();

        initData();
    }

    private void initView(){
        btn_wheel_cancel = (Button) mView.findViewById(R.id.btn_wheel_cancel);
        btn_wheel_confirm = (Button) mView.findViewById(R.id.btn_wheel_confirm);
        tv_wheel_title = (TextView) mView.findViewById(R.id.tv_wheel_title);
        mWheelView = (WheelView) mView.findViewById(R.id.wheel_content);
        mWheelView1 = (WheelView) mView.findViewById(R.id.wheel_content1);
        mWheelView1.setVisibility(View.GONE);
    }

    private void initData(){
        if (mContents != null && mContents.size() > 0) {
            mWheelView.setItemViewPadding(0, 0, 0, 0);
            mWheelView.setViewAdapter(new ArrayWheelAdapter<String>(mContext, (String [])mContents.toArray()));
            mWheelView.setVisibleItems(7);
            mWheelView.setCurrentItem(0);
            setWheelViewCyclic(false);
            content = mContents.get(0);
        }

        if (mContents1 != null && mContents1.size() > 0) {
            mWheelView.setItemViewPadding(200, 0, 0, 0);

            mWheelView1.setItemViewPadding(0, 0, 200, 0);
            mWheelView1.setViewAdapter(new ArrayWheelAdapter<String>(mContext, (String [])mContents1.toArray()));
            mWheelView1.setVisibility(View.VISIBLE);
            mWheelView1.setVisibleItems(7);
            mWheelView1.setCurrentItem(0);
        }
    }

    public String getCurrentItem(){
        return mContents.get(mWheelView.getCurrentItem());
    }

    public int getCurrentItemID(){
        return mWheelView.getCurrentItem();
    }

    public void setCurrentItem(String currentItem){
        if (mContents != null && mContents.size()>0) {
            for (int i = 0; i < mContents.size(); i++) {
                if (mContents.get(i)!=null && mContents.get(i).equals(currentItem)){
                    mWheelView.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public void setCurrentItem(int currentItem){
        if (mContents != null && mContents.size()>currentItem) {
            mWheelView.setCurrentItem(currentItem);
        }
    }

    public String getCurrentItem1(){
        return mContents1.get(mWheelView1.getCurrentItem());
    }

    public int getCurrentItemID1(){
        return mWheelView1.getCurrentItem();
    }

    public void setCurrentItem1(String currentItem){
        if (mContents1 != null && mContents1.size()>0) {
            for (int i = 0; i < mContents1.size(); i++) {
                if (mContents1.get(i)!=null && mContents1.get(i).equals(currentItem)){
                    mWheelView1.setCurrentItem(i);
                    return;
                }
            }
        }
    }

    public void setCurrentItem1(int currentItem){
        if (mContents1 != null && mContents1.size()>currentItem) {
            mWheelView1.setCurrentItem(currentItem);
        }
    }

    public void setWheelViewCyclic(boolean isCyclic){
        mWheelView.setCyclic(isCyclic);
    }

    public void setWheelViewCyclic1(boolean isCyclic){
        mWheelView1.setCyclic(isCyclic);
    }
}
