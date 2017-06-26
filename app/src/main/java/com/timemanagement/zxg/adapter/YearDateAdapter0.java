package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.timemanagement.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/2/12.
 */

public class YearDateAdapter0 extends RecyclerView.Adapter<YearDateAdapter0.RecyclerViewHolder> {

    private Context mContext;
    private List<MonthDateModel> monthDateModels;
    private Calendar mCalendar;

    public YearDateAdapter0(Context context, List<MonthDateModel> monthDateModels){
        mContext = context;
        this.monthDateModels = monthDateModels;
        mCalendar = Calendar.getInstance();
    }

    public YearDateAdapter0(Context context){
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_year_month, parent, false);
        YearDateAdapter0.RecyclerViewHolder holder = new YearDateAdapter0.RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        List<DayDateModel> dayDateModels = monthDateModels.get(position%monthDateModels.size()).getDayDateModels();
        int offset = Integer.valueOf(dayDateModels.get(0).getWeek());

        //2月只有28天，且2月1号为周日时，隐藏最后一行
        if (offset ==0 && dayDateModels.size() == 28){
            viewHolder.itemViews[4].setVisibility(View.GONE);
        } else {
            viewHolder.itemViews[4].setVisibility(View.VISIBLE);
        }

        if (offset+dayDateModels.size() <= 35){
            if (viewHolder.itemViews[5] != null) {
                viewHolder.itemViews[5].setVisibility(View.GONE);
            }
        } else {
            if (viewHolder.itemViews[5] != null) {
                viewHolder.itemViews[5].setVisibility(View.VISIBLE);
            }
        }

        viewHolder.tv_month.setText(monthDateModels.get(position%monthDateModels.size()).getMonth()+"月");
        for (int i = 0; i < offset; i++) {
            viewHolder.tvDays[i].setText("");
        }

        for (int i = offset; i < dayDateModels.size()+offset; i++) {
            viewHolder.tvDays[i].setText(dayDateModels.get(i-offset).getDay());
            if (dayDateModels.get(i-offset).getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                    && dayDateModels.get(i-offset).getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                    && dayDateModels.get(i-offset).getDay().equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")){
                viewHolder.tvDays[i].setBackgroundResource(R.drawable.bg_circular_red);
                viewHolder.tvDays[i].setTextColor(Color.rgb(255, 255, 255));
            } else {
                viewHolder.tvDays[i].setBackgroundColor(Color.WHITE);
                viewHolder.tvDays[i].setTextColor(Color.BLACK);
            }
        }

        for (int i = dayDateModels.size()+offset; i < 42; i++) {
            viewHolder.tvDays[i].setText("");
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public MonthDateModel getItem(int position) {
        return monthDateModels.get(position);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView tv_month;

        View[] itemViews = new View[6];

        TextView[] tvDays = new TextView[42];

        public RecyclerViewHolder(View convertView) {
            super(convertView);
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
        }
    }
}