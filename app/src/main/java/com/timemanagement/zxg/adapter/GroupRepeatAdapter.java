package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 17/2/7.
 */

public class GroupRepeatAdapter extends BaseAdapter{

    private Context mContext;
    private List<EventModel> list_group = new ArrayList<EventModel>();

    public GroupRepeatAdapter(Context context, List<EventModel> list_group){
        mContext = context;
        this.list_group = list_group;
    }

    @Override
    public int getCount() {
        return list_group.size();
    }

    @Override
    public EventModel getItem(int position) {
        return list_group.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.list_event_repeat_items, null);
            viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_start_time.setText(TimeUtils.dateToStrShort(getItem(position).getRemind()));
        if (getItem(position).getRepeatEnd() == null) {
            viewHolder.tv_end_time.setText("+∞");
        } else {
            viewHolder.tv_end_time.setText(TimeUtils.dateToStrShort(getItem(position).getRepeatEnd()));
        }
        viewHolder.tv_content.setText(getItem(position).getTitle());

        return convertView;
    }


    class ViewHolder{
        TextView tv_start_time, tv_end_time, tv_content;
    }
}
