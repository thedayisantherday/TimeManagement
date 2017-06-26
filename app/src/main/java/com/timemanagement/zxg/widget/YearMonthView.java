package com.timemanagement.zxg.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/6/13.
 */

public class YearMonthView extends LinearLayout {

    private static String TAG = YearMonthView.class.getSimpleName();

    private Context mContext;
    private Calendar mCalendar;

    private TextView tv_month;
    private View[] itemViews = new View[6];
    private TextView[] tvDays = new TextView[42];

    public YearMonthView(Context context) {
        super(context);
        mContext = context;
        mCalendar = Calendar.getInstance();
        initView();
    }

    public YearMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mCalendar = Calendar.getInstance();
        initView();
    }

    public YearMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mCalendar = Calendar.getInstance();
        initView();
    }

    private void initView() {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_year_month, null);
        tv_month = (TextView) convertView.findViewById(R.id.tv_month);

        itemViews[0] = convertView.findViewById(R.id.layout_month_item0);
        itemViews[1] = convertView.findViewById(R.id.layout_month_item1);
        itemViews[2] = convertView.findViewById(R.id.layout_month_item2);
        itemViews[3] = convertView.findViewById(R.id.layout_month_item3);
        itemViews[4] = convertView.findViewById(R.id.layout_month_item4);
        itemViews[5] = convertView.findViewById(R.id.layout_month_item5);

        for (int i = 0; i < itemViews.length; i++) {
            tvDays[i * 7] = (TextView) itemViews[i].findViewById(R.id.tv_day0);

            tvDays[i * 7 + 1] = (TextView) itemViews[i].findViewById(R.id.tv_day1);

            tvDays[i * 7 + 2] = (TextView) itemViews[i].findViewById(R.id.tv_day2);

            tvDays[i * 7 + 3] = (TextView) itemViews[i].findViewById(R.id.tv_day3);

            tvDays[i * 7 + 4] = (TextView) itemViews[i].findViewById(R.id.tv_day4);

            tvDays[i * 7 + 5] = (TextView) itemViews[i].findViewById(R.id.tv_day5);

            tvDays[i * 7 + 6] = (TextView) itemViews[i].findViewById(R.id.tv_day6);
        }
        this.addView(convertView);
    }

    public void setData(List<DayDateModel> dayDateModels) {
        if (dayDateModels == null || dayDateModels.size() <= 0) {
            return;
        }

        int offset = Integer.valueOf(dayDateModels.get(0).getWeek());
        LogUtils.i(TAG, "setData offset:" + offset);

        //2月只有28天，且2月1号为周日时，隐藏最后一行
        if (offset ==0 && dayDateModels.size() == 28){
            itemViews[4].setVisibility(View.GONE);
        } else {
            itemViews[4].setVisibility(View.VISIBLE);
        }

        if (offset+dayDateModels.size() <= 35){
            if (itemViews[5] != null) {
                itemViews[5].setVisibility(View.GONE);
            }
        } else {
            if (itemViews[5] != null) {
                itemViews[5].setVisibility(View.VISIBLE);
            }
        }

        tv_month.setText(dayDateModels.get(0).getMonth()+"月");
        for (int i = 0; i < offset; i++) {
            tvDays[i].setText("");
        }

        for (int i = offset; i < dayDateModels.size()+offset; i++) {
            tvDays[i].setText(dayDateModels.get(i-offset).getDay());
            if (dayDateModels.get(i-offset).getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                    && dayDateModels.get(i-offset).getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                    && dayDateModels.get(i-offset).getDay().equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")){
                tvDays[i].setBackgroundResource(R.drawable.bg_circular_red);
                tvDays[i].setTextColor(Color.rgb(255, 255, 255));
            } else {
                tvDays[i].setBackgroundColor(Color.WHITE);
                tvDays[i].setTextColor(Color.BLACK);
            }
        }

        for (int i = dayDateModels.size()+offset; i < 42; i++) {
            tvDays[i].setText("");
        }
    }
}
