package com.timemanagement.zxg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.utils.DimensionUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zhuxiaoguang on 2018/1/9.
 */

public class YearMonthView0 extends View {

    private final int ITEM_SIZE = DimensionUtils.sp2px(8);
    private MonthDateModel mMonthDateModel;
    private Calendar mCalendar;
    private int mWidth, mItemWidth, offset, rows;
    private Paint mMonthPaint, mBGPaint, mDayPaint;

    public YearMonthView0(Context context) {
        super(context);
        init();
    }

    public YearMonthView0(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YearMonthView0(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init () {
        mWidth = (DimensionUtils.getWidthPixels() - DimensionUtils.dp2px(40)) / 3;
        mItemWidth = mWidth / 7;
        mCalendar = Calendar.getInstance();

        mMonthPaint = new Paint();
        mMonthPaint.setAntiAlias(true);
        mMonthPaint.setDither(true);
        mMonthPaint.setStyle(Paint.Style.FILL);
        mMonthPaint.setTextSize(DimensionUtils.sp2px(13));
        mMonthPaint.setColor(Color.RED);

        mBGPaint = new Paint();
        mBGPaint.setAntiAlias(true);
        mBGPaint.setDither(true);
        mBGPaint.setStyle(Paint.Style.FILL);
        mBGPaint.setColor(Color.RED);

        mDayPaint = new Paint();
        mDayPaint.setAntiAlias(true);
        mDayPaint.setDither(true);
        mDayPaint.setStyle(Paint.Style.FILL);
        mDayPaint.setTextSize(ITEM_SIZE);
        mDayPaint.setColor(Color.BLACK);
    }

    public void setMonthDateModel (MonthDateModel monthDateModel) {
        mMonthDateModel = monthDateModel;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecSize = mWidth;
        if (mMonthDateModel != null && mMonthDateModel.getDayDateModels() != null && mMonthDateModel.getDayDateModels().size() >= 28) {
            int offset = Integer.valueOf(mMonthDateModel.getDayDateModels().get(0).getWeek());
            int size = mMonthDateModel.getDayDateModels().size();
            rows = (offset+size)/7 + (0==(offset+size)%7 ? 2 : 3);
        }
        int heightSpecSize = rows * mItemWidth;
        // 设置宽度和高度
        setMeasuredDimension(widthSpecSize,heightSpecSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMonthDateModel != null && mMonthDateModel.getDayDateModels() != null && mMonthDateModel.getDayDateModels().size() >= 28) {
            // 绘制月份
            canvas.drawText(mMonthDateModel.getMonth() + "月", 0, mItemWidth, mMonthPaint);
            // 绘制阳历天
            drawDay(canvas);
        }
    }

    private void drawDay (Canvas canvas) {
        List<DayDateModel> dayDateModels = mMonthDateModel.getDayDateModels();
        offset = Integer.valueOf(dayDateModels.get(0).getWeek());
        float row, column;
        for (int i = 0; i < dayDateModels.size(); i++) {
            row = 1.8f + (i+offset) / 7; // 行号
            column = (i+offset) % 7; // 列号
            String dayStr = dayDateModels.get(i).getDay();
            // 日期为当前日期时，设置红色背景颜色
            if (dayDateModels.get(i).getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                    && dayDateModels.get(i).getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                    && dayStr.equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")){
                canvas.drawCircle((column+0.5f)*mItemWidth - ITEM_SIZE*0.125f, row * mItemWidth, 0.5f * mItemWidth, mBGPaint);
            }

            float offsetX = (mItemWidth - ITEM_SIZE) * 0.5f;
            if (dayStr.length() > 1) {
                offsetX -= ITEM_SIZE*0.25f;
            }
            float itemY = row * mItemWidth + ITEM_SIZE * 0.5f;
            canvas.drawText(dayStr, offsetX + column * mItemWidth, itemY, mDayPaint);
        }
    }
}
