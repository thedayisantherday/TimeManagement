package com.timemanagement.zxg.common;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by zhuxiaoguang on 2018/1/18.
 */

public class CustomLayoutManagement extends LinearLayoutManager {

    private float MILLISECONDS_PER_INCH = 3f;
    private Context contxt;

    public CustomLayoutManagement(Context context) {
        super(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                return boxStart-viewStart;//返回的就是我们item置顶需要的偏移量
            }
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return CustomLayoutManagement.this.computeScrollVectorForPosition(targetPosition);
            }

            //This returns the milliseconds it takes to
            //scroll one pixel.
            @Override
            protected float calculateSpeedPerPixel (DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.density;
                //返回滑动一个pixel需要多少毫秒
            }

        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    public void setSpeedSlow() {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = contxt.getResources().getDisplayMetrics().density * 0.03f;
    }
}
