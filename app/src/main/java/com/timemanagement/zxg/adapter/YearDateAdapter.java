package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.timemanagement.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/2/12.
 */

public class YearDateAdapter extends BaseAdapter {

    private Context mContext;
//    private Calendar calendar;
    private List<MonthDateModel> monthDateModels;
    private Calendar mCalendar;

    public YearDateAdapter(Context context, List<MonthDateModel> monthDateModels){
        mContext = context;
//        calendar = Calendar.getInstance();
        this.monthDateModels = monthDateModels;
        mCalendar = Calendar.getInstance();
    }

    public YearDateAdapter(Context context){
        mContext = context;
//        calendar = Calendar.getInstance();
    }

    @Override
    public int getCount() {
        return 12;
    }

    @Override
    public Object getItem(int position) {
        return monthDateModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int postition, View convertView, ViewGroup arg2) {
        ViewHoler viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHoler();
            convertView = View.inflate(mContext, R.layout.layout_year_month, null);
//          convertView.setLayoutParams(new GridView.LayoutParams(200, 900));

            viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);

            viewHolder.itemViews[0] = convertView.findViewById(R.id.layout_month_item0);
            viewHolder.itemViews[1] = convertView.findViewById(R.id.layout_month_item1);
            viewHolder.itemViews[2] = convertView.findViewById(R.id.layout_month_item2);
            viewHolder.itemViews[3] = convertView.findViewById(R.id.layout_month_item3);
            viewHolder.itemViews[4] = convertView.findViewById(R.id.layout_month_item4);
            viewHolder.itemViews[5] = convertView.findViewById(R.id.layout_month_item5);

            for (int i = 0; i < viewHolder.itemViews.length; i++) {
                viewHolder.tvDays[i * 7] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day0);

                viewHolder.tvDays[i * 7 + 1] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day1);

                viewHolder.tvDays[i * 7 + 2] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day2);

                viewHolder.tvDays[i * 7 + 3] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day3);

                viewHolder.tvDays[i * 7 + 4] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day4);

                viewHolder.tvDays[i * 7 + 5] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day5);

                viewHolder.tvDays[i * 7 + 6] = (TextView) viewHolder.itemViews[i].findViewById(R.id.tv_day6);
            }
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHoler) convertView.getTag();
        }

        List<DayDateModel> dayDateModels = monthDateModels.get(postition%monthDateModels.size()).getDayDateModels();
        int offset = Integer.valueOf(dayDateModels.get(0).getWeek());

//        LinearLayout.LayoutParams lp_month
//                = (LinearLayout.LayoutParams)viewHolder.tv_month.getLayoutParams();
//        lp_month.setMargins(DimensionUtils.getWidthPixels()/7 * offset+30, 0, 20, 20);
//        viewHolder.tv_month.setLayoutParams(lp_month);

        //2月只有28天，且2月1号为周日时，隐藏最后一行
        if (offset ==0 && dayDateModels.size() == 28){
            viewHolder.itemViews[4].setVisibility(View.GONE);
        }

        if (offset+dayDateModels.size() <= 35){
            if (viewHolder.itemViews[5] != null) {
                viewHolder.itemViews[5].setVisibility(View.GONE);
            }
        }

        viewHolder.tv_month.setText(/*monthDateModels.get(postition%monthDateModels.size()).getYear()
                +*/monthDateModels.get(postition%monthDateModels.size()).getMonth()+"月");
        for (int i = 0; i < offset; i++) {
            viewHolder.tvDays[i].setTextColor(Color.rgb(255,255,255));
        }

        for (int i = offset; i < dayDateModels.size()+offset; i++) {
            viewHolder.tvDays[i].setText(dayDateModels.get(i-offset).getDay());
            if (dayDateModels.get(i-offset).getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                    && dayDateModels.get(i-offset).getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                    && dayDateModels.get(i-offset).getDay().equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")){
                viewHolder.tvDays[i].setBackgroundResource(R.drawable.bg_circular_red);
                viewHolder.tvDays[i].setTextColor(Color.rgb(255, 255, 255));
            }
        }

        for (int i = dayDateModels.size()+offset; i < 42; i++) {
            viewHolder.tvDays[i].setText("");
        }

        return convertView;
    }

    static class ViewHoler{

        TextView tv_month;

        View[] itemViews = new View[6];

        TextView[] tvDays = new TextView[42];
    }
}