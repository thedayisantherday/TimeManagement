package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.timemanagement.zxg.activities.EventMonthActivity;
import com.timemanagement.zxg.activities.EventYearActivity;
import com.timemanagement.zxg.model.YearDateModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.LogUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zxg on 17/2/12.
 */

public class EventYearAdapter extends BaseAdapter {

    private Context mContext;
    private Calendar calendar;
    private List<YearDateModel> yearDateModels;
    private YearDateAdapter yearDateAdapter;

    public EventYearAdapter(Context context, List<YearDateModel> yearDateModels){
        mContext = context;
        calendar = Calendar.getInstance();
        this.yearDateModels = yearDateModels;
    }

    public EventYearAdapter(Context context){
        mContext = context;
        calendar = Calendar.getInstance();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public YearDateModel getItem(int position) {
        return yearDateModels.get(position%yearDateModels.size());
    }

    @Override
    public long getItemId(int position) {
        return position%yearDateModels.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        LogUtils.i("gridview","position:"+position);
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.layout_event_year, null);
            viewHolder.tv_year = (TextView) convertView.findViewById(R.id.tv_year);
            viewHolder.tv_lunar_year = (TextView) convertView.findViewById(R.id.tv_lunar_year);
            viewHolder.gl_year = (GridView) convertView.findViewById(R.id.gl_year);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.gl_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                LogUtils.i("gridview","year:"+getItem(position).getMonthDateModels().get(pos).getYear()+", month:"+
                        getItem(position).getMonthDateModels().get(pos).getMonth());
                EventMonthActivity.startSelf(mContext, Integer.valueOf(getItem(position).getMonthDateModels().get(pos).getYear()),
                        Integer.valueOf(getItem(position).getMonthDateModels().get(pos).getMonth()));
                ((EventYearActivity)mContext).finish();
            }
        });

        YearDateModel yearDateModel = yearDateModels.get(position%yearDateModels.size());
        viewHolder.tv_year.setText(yearDateModel.getYear()+"年");
        viewHolder.tv_lunar_year.setText(yearDateModel.getLunarYear()+"年");

        yearDateAdapter = new YearDateAdapter(mContext, yearDateModel.getMonthDateModels());
        viewHolder.gl_year.setAdapter(yearDateAdapter);

        return convertView;
    }

    private class ViewHolder {
        public TextView tv_year, tv_lunar_year;
        public GridView gl_year;
    }
}
