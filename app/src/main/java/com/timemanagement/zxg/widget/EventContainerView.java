package com.timemanagement.zxg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.utils.UIUtils;

import java.util.Calendar;

/**
 * Created by zxg on 17/4/10.
 */

public class EventContainerView extends RelativeLayout {

    private Context mContext;
    private GestureDetector mGestureDetector;
    private LinearLayout ll_event_container;

    public int mTotalHeight;
    public int offset;
    public int mCurTimeHeight;
    public static final int MINITES_OF_DAY = 24 * 60;

    private int startX, startY;
    public final int MIN_MOVE_DISTANCE = 10;

    private OnLongPressListener mOnLongPressListener;
    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        this.mOnLongPressListener = onLongPressListener;
    }

    public interface OnLongPressListener {
        void onLongPress(float pressX, float pressY);
    }

    public EventContainerView(Context context) {
        super(context);
        init(context);
    }

    public EventContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_event_contain, this, true);
        ll_event_container = (LinearLayout) findViewById(R.id.ll_event_container);
        mGestureDetector = new GestureDetector(mContext, new EventGestureDetector());

//        final ViewGroup self = this;
        /**
         * 使用观察者模式获取rl_event_container的高度，App刚启动时在EventDayActivity中依旧获取不到高度
         * 通过计算得到当前时间对应的位置
         */
//        ViewTreeObserver vto = self.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                self.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                Calendar _calendar = Calendar.getInstance();
//                int _hour = _calendar.get(Calendar.HOUR_OF_DAY);
//                int _minute = _calendar.get(Calendar.MINUTE);
//
//                View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_event_day_now, null);
//                TextView tv_current_time = (TextView) mView.findViewById(R.id.tv_current_time);
//                tv_current_time.setText(""/*Tools.formatTime(_hour)+":"+Tools.formatTime(_minute)*/);
//                offset = UIUtils.getViewWidthAndHeight(mView)[1];
//                mTotalHeight = self.getHeight() - offset;
//
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
//                mCurTimeHeight = mTotalHeight * (_hour*60+_minute) / MINITES_OF_DAY;
//                LogUtils.i("+++mHeight:", "mTotalHeight:"+mTotalHeight+", _hour"+_hour+",_minute"+_minute);
//                layoutParams.setMargins(0, mCurTimeHeight, 0, 0);
//                mView.setLayoutParams(layoutParams);
//                self.addView(mView);
//            }
//        });

        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_event_day_now, null);
        TextView tv_current_time = (TextView) mView.findViewById(R.id.tv_current_time);
        tv_current_time.setText("");
        offset = UIUtils.getViewWidthAndHeight(mView)[1];
        mTotalHeight = UIUtils.getViewWidthAndHeight(this)[1] - offset;

        Calendar _calendar = Calendar.getInstance();
        int _hour = _calendar.get(Calendar.HOUR_OF_DAY);
        int _minute = _calendar.get(Calendar.MINUTE);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        mCurTimeHeight = mTotalHeight * (_hour*60+_minute) / MINITES_OF_DAY;
        LogUtils.i("+++mHeight:", "mTotalHeight:"+mTotalHeight+", _hour"+_hour+",_minute"+_minute);
        layoutParams.setMargins(0, mCurTimeHeight, 0, 0);
        mView.setLayoutParams(layoutParams);
        this.addView(mView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startX = (int)event.getX();
        startY = (int)event.getY();
        LogUtils.i("+++onInterceptTouchEvent1:", "startX:"+startX+",startY:"+startY);
        return mGestureDetector.onTouchEvent(event);
    }

    class EventGestureDetector implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            LogUtils.i("GestureDetector", "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            LogUtils.i("GestureDetector", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            LogUtils.i("GestureDetector", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtils.i("GestureDetector", "onScroll");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            LogUtils.i("+++onLongPress:", "e.getX():"+e.getX()+",e.getY():"+e.getY());
            if ((int)Math.abs(e.getX()-startX) < MIN_MOVE_DISTANCE
                        && (int)Math.abs(e.getY()-startY) < MIN_MOVE_DISTANCE){
                mOnLongPressListener.onLongPress(e.getX(), e.getY());
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtils.i("GestureDetector", "onFling");
            return false;
        }
    }
}
