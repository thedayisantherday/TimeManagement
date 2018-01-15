package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventMonthActivity;
import com.timemanagement.zxg.activities.EventYearActivity;
import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.activities.activitycontrol.ActivityManager;
import com.timemanagement.zxg.model.MonthDateModel;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.widget.YearMonthView;
import com.timemanagement.zxg.widget.YearMonthView0;

import java.util.List;

/**
 * Created by zxg on 17/6/8.
 */

public class EventYearAdapter1 extends RecyclerView.Adapter<EventYearAdapter1.RecyclerViewHolder> {
    private Context mContext;
    private List<YearDateModel> yearDateModels;

    public EventYearAdapter1(Context context, List<YearDateModel> yearDateModels){
        mContext = context;
        this.yearDateModels = yearDateModels;
    }

    public EventYearAdapter1(Context context){
        mContext = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_event_year1, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int position) {

        final YearDateModel yearDateModel = getItem(position);
        viewHolder.tv_year.setText(yearDateModel.getYear()+"年");
        viewHolder.tv_lunar_year.setText(yearDateModel.getLunarYear()+"年");
        for (int i = 0; i < yearDateModel.getMonthDateModels().size(); i++) {
            final MonthDateModel monthDateModel = yearDateModel.getMonthDateModels().get(i);
            viewHolder.year_month[i].setMonthDateModel(monthDateModel);
            viewHolder.year_month[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("year",  Integer.valueOf(monthDateModel.getYear()));
                    bundle.putInt("month",  Integer.valueOf(monthDateModel.getMonth()));
                    ((MainActivity)mContext).setFragment(-1, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public YearDateModel getItem(int position) {
        if (yearDateModels == null || yearDateModels.size() == 0){
            return null;
        }
        int _position = position%yearDateModels.size();
        if (_position < 0){
            _position = _position + yearDateModels.size();
        }
        return yearDateModels.get(_position);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_year, tv_lunar_year;
        public YearMonthView0[] year_month = new YearMonthView0[12];

        public RecyclerViewHolder(View convertView) {
            super(convertView);
            tv_year = (TextView) convertView.findViewById(R.id.tv_year);
            tv_lunar_year = (TextView) convertView.findViewById(R.id.tv_lunar_year);

            year_month[0] = (YearMonthView0) convertView.findViewById(R.id.year_month_item0);
            year_month[1] = (YearMonthView0) convertView.findViewById(R.id.year_month_item1);
            year_month[2] = (YearMonthView0) convertView.findViewById(R.id.year_month_item2);
            year_month[3] = (YearMonthView0) convertView.findViewById(R.id.year_month_item3);
            year_month[4] = (YearMonthView0) convertView.findViewById(R.id.year_month_item4);
            year_month[5] = (YearMonthView0) convertView.findViewById(R.id.year_month_item5);
            year_month[6] = (YearMonthView0) convertView.findViewById(R.id.year_month_item6);
            year_month[7] = (YearMonthView0) convertView.findViewById(R.id.year_month_item7);
            year_month[8] = (YearMonthView0) convertView.findViewById(R.id.year_month_item8);
            year_month[9] = (YearMonthView0) convertView.findViewById(R.id.year_month_item9);
            year_month[10] = (YearMonthView0) convertView.findViewById(R.id.year_month_item10);
            year_month[11] = (YearMonthView0) convertView.findViewById(R.id.year_month_item11);
        }
    }
}
