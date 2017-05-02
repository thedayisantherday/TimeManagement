package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventDayActivity;
import com.timemanagement.zxg.activities.EventMonthActivity;
import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.DimensionUtils;
import com.timemanagement.zxg.utils.Tools;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/2/12.
 */

public class EventMonthAdapter extends BaseAdapter {

    private Context mContext;
    private Calendar calendar;
    private List<MonthDateModel> monthDateModels;
    private Calendar mCalendar;

    public EventMonthAdapter(Context context, List<MonthDateModel> monthDateModels){
        mContext = context;
        calendar = Calendar.getInstance();
        this.monthDateModels = monthDateModels;

        mCalendar = Calendar.getInstance();
    }

    public EventMonthAdapter(Context context, int year, int month){
        mContext = context;
        calendar = Calendar.getInstance();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public MonthDateModel getItem(int position) {
        if (monthDateModels == null || monthDateModels.size() == 0){
            return null;
        }
        return monthDateModels.get(position%monthDateModels.size());
    }

    @Override
    public long getItemId(int position) {
        return position%monthDateModels.size();
    }

    @Override
    public View getView(int postition, View convertView, ViewGroup arg2) {
        ViewHoler viewHolder = null;
        View[] itemViews = new View[6];

        /*if(convertView == null){
            viewHolder = new ViewHoler();
            convertView = View.inflate(mContext, R.layout.layout_event_month, null);

            viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            viewHolder.view_line_top = convertView.findViewById(R.id.view_line_top);
            viewHolder.view_line_bottom = convertView.findViewById(R.id.view_line_bottom);
            viewHolder.view_line_bottom0 = convertView.findViewById(R.id.view_line_bottom0);

            itemViews[0] = convertView.findViewById(R.id.layout_month_item0);
            itemViews[1] = convertView.findViewById(R.id.layout_month_item1);
            itemViews[2] = convertView.findViewById(R.id.layout_month_item2);
            itemViews[3] = convertView.findViewById(R.id.layout_month_item3);
            itemViews[4] = convertView.findViewById(R.id.layout_month_item4);
            itemViews[5] = convertView.findViewById(R.id.layout_month_item5);

            for (int i = 0; i < itemViews.length; i++) {
                viewHolder.tvDays[i*7] = (TextView)itemViews[i].findViewById(R.id.tv_day0);
                viewHolder.tvLunars[i*7] = (TextView)itemViews[i].findViewById(R.id.tv_lunar0);

                viewHolder.tvDays[i*7+1] = (TextView)itemViews[i].findViewById(R.id.tv_day1);
                viewHolder.tvLunars[i*7+1] = (TextView)itemViews[i].findViewById(R.id.tv_lunar1);

                viewHolder.tvDays[i*7+2] = (TextView)itemViews[i].findViewById(R.id.tv_day2);
                viewHolder.tvLunars[i*7+2] = (TextView)itemViews[i].findViewById(R.id.tv_lunar2);

                viewHolder.tvDays[i*7+3] = (TextView)itemViews[i].findViewById(R.id.tv_day3);
                viewHolder.tvLunars[i*7+3] = (TextView)itemViews[i].findViewById(R.id.tv_lunar3);

                viewHolder.tvDays[i*7+4] = (TextView)itemViews[i].findViewById(R.id.tv_day4);
                viewHolder.tvLunars[i*7+4] = (TextView)itemViews[i].findViewById(R.id.tv_lunar4);

                viewHolder.tvDays[i*7+5] = (TextView)itemViews[i].findViewById(R.id.tv_day5);
                viewHolder.tvLunars[i*7+5] = (TextView)itemViews[i].findViewById(R.id.tv_lunar5);

                viewHolder.tvDays[i*7+6] = (TextView)itemViews[i].findViewById(R.id.tv_day6);
                viewHolder.tvLunars[i*7+6] = (TextView)itemViews[i].findViewById(R.id.tv_lunar6);
            }

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHoler) convertView.getTag();
        }*/

        viewHolder = new ViewHoler();
        convertView = View.inflate(mContext, R.layout.layout_event_month, null);

        viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
        viewHolder.view_line_top = convertView.findViewById(R.id.view_line_top);
        viewHolder.view_line_bottom = convertView.findViewById(R.id.view_line_bottom);
        viewHolder.view_line_bottom0 = convertView.findViewById(R.id.view_line_bottom0);

        itemViews[0] = convertView.findViewById(R.id.layout_month_item0);
        itemViews[1] = convertView.findViewById(R.id.layout_month_item1);
        itemViews[2] = convertView.findViewById(R.id.layout_month_item2);
        itemViews[3] = convertView.findViewById(R.id.layout_month_item3);
        itemViews[4] = convertView.findViewById(R.id.layout_month_item4);
        itemViews[5] = convertView.findViewById(R.id.layout_month_item5);

        for (int i = 0; i < itemViews.length; i++) {
            viewHolder.llDays[i*7] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day0);
            viewHolder.tvDays[i*7] = (TextView)itemViews[i].findViewById(R.id.tv_day0);
            viewHolder.tvLunars[i*7] = (TextView)itemViews[i].findViewById(R.id.tv_lunar0);

            viewHolder.llDays[i*7+1] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day1);
            viewHolder.tvDays[i*7+1] = (TextView)itemViews[i].findViewById(R.id.tv_day1);
            viewHolder.tvLunars[i*7+1] = (TextView)itemViews[i].findViewById(R.id.tv_lunar1);

            viewHolder.llDays[i*7+2] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day2);
            viewHolder.tvDays[i*7+2] = (TextView)itemViews[i].findViewById(R.id.tv_day2);
            viewHolder.tvLunars[i*7+2] = (TextView)itemViews[i].findViewById(R.id.tv_lunar2);

            viewHolder.llDays[i*7+3] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day3);
            viewHolder.tvDays[i*7+3] = (TextView)itemViews[i].findViewById(R.id.tv_day3);
            viewHolder.tvLunars[i*7+3] = (TextView)itemViews[i].findViewById(R.id.tv_lunar3);

            viewHolder.llDays[i*7+4] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day4);
            viewHolder.tvDays[i*7+4] = (TextView)itemViews[i].findViewById(R.id.tv_day4);
            viewHolder.tvLunars[i*7+4] = (TextView)itemViews[i].findViewById(R.id.tv_lunar4);

            viewHolder.llDays[i*7+5] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day5);
            viewHolder.tvDays[i*7+5] = (TextView)itemViews[i].findViewById(R.id.tv_day5);
            viewHolder.tvLunars[i*7+5] = (TextView)itemViews[i].findViewById(R.id.tv_lunar5);

            viewHolder.llDays[i*7+6] = (LinearLayout) itemViews[i].findViewById(R.id.ll_day6);
            viewHolder.tvDays[i*7+6] = (TextView)itemViews[i].findViewById(R.id.tv_day6);
            viewHolder.tvLunars[i*7+6] = (TextView)itemViews[i].findViewById(R.id.tv_lunar6);
        }

        List<DayDateModel> dayDateModels = monthDateModels.get(postition%monthDateModels.size()).getDayDateModels();
        int offset = Integer.valueOf(dayDateModels.get(0).getWeek());

        LinearLayout.LayoutParams lp_month
                = (LinearLayout.LayoutParams)viewHolder.tv_month.getLayoutParams();
        lp_month.setMargins(DimensionUtils.getWidthPixels()/7 * offset+30, 0, 20, 20);
        viewHolder.tv_month.setLayoutParams(lp_month);
        LinearLayout.LayoutParams lp_top
                = (LinearLayout.LayoutParams)viewHolder.view_line_top.getLayoutParams();
        lp_top.setMargins(DimensionUtils.getWidthPixels()/7 * offset, 0, 0, 10);
        viewHolder.view_line_top.setLayoutParams(lp_top);

        //2月只有28天，且2月1号为周日时，隐藏最后一行
        if (offset ==0 && dayDateModels.size() == 28){
            itemViews[4].setVisibility(View.GONE);
            viewHolder.view_line_bottom.setVisibility(View.GONE);
        }

        if (offset+dayDateModels.size() <= 35){
            LinearLayout.LayoutParams lp_bottom
                    = (LinearLayout.LayoutParams)viewHolder.view_line_bottom.getLayoutParams();
            lp_bottom.setMargins(0, 0, (35-offset-dayDateModels.size())*DimensionUtils.getWidthPixels()/7, 10);
            viewHolder.view_line_bottom.setLayoutParams(lp_bottom);

            viewHolder.view_line_bottom0.setVisibility(View.GONE);
            if (itemViews[5] != null) {
                itemViews[5].setVisibility(View.GONE);
            }
        } else {
            LinearLayout.LayoutParams lp_bottom
                    = (LinearLayout.LayoutParams)viewHolder.view_line_bottom0.getLayoutParams();
            lp_bottom.setMargins(0, 0, (42-offset-dayDateModels.size())*DimensionUtils.getWidthPixels()/7, 10);
            viewHolder.view_line_bottom0.setLayoutParams(lp_bottom);
        }

        viewHolder.tv_month.setText(/*monthDateModels.get(postition%monthDateModels.size()).getYear()
                +*/monthDateModels.get(postition%monthDateModels.size()).getMonth()+"月");
        for (int i = 0; i < offset; i++) {
            viewHolder.tvDays[i].setTextColor(Color.rgb(255,255,255));
            viewHolder.tvLunars[i].setTextColor(Color.rgb(255,255,255));
        }

        for (int i = offset; i < dayDateModels.size()+offset; i++) {
            final DayDateModel _dayDateModel = dayDateModels.get(i-offset);
            viewHolder.tvDays[i].setText(_dayDateModel.getDay());
            String _str = _dayDateModel.getLunar();
            if (!Tools.isEmpty(_str) && _str.lastIndexOf("月")>=0) {
                if (_str.contains("初一") && _str.lastIndexOf("年")>=0) {
                    viewHolder.tvLunars[i].setText(_str.substring(_str.lastIndexOf("年")+1, _str.lastIndexOf("月")+1));
                } else {
                    viewHolder.tvLunars[i].setText(_str.substring(_str.lastIndexOf("月")+1));
                }
            }
            if (_dayDateModel.getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                    && _dayDateModel.getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                    && _dayDateModel.getDay().equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")){
                viewHolder.llDays[i].setBackgroundResource(R.drawable.bg_circular_red);
                viewHolder.tvDays[i].setTextColor(Color.rgb(255, 255, 255));
                viewHolder.tvLunars[i].setTextColor(Color.rgb(255, 255, 255));
            }
            viewHolder.llDays[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventDayActivity.startSelf(mContext, _dayDateModel, null);
                    ((EventMonthActivity)mContext).finish();
                }
            });
        }

        for (int i = dayDateModels.size()+offset; i < 42; i++) {
            viewHolder.tvDays[i].setText("");
            viewHolder.tvLunars[i].setText("");
        }

        return convertView;
    }

    static class ViewHoler{

        TextView tv_month;

        LinearLayout[] llDays = new LinearLayout[42];
        TextView[] tvDays = new TextView[42];
        TextView[] tvLunars = new TextView[42];

        View view_line_top;
        View view_line_bottom;
        View view_line_bottom0;
    }
}
