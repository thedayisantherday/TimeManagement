package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 17/2/7.
 */

public class GroupListAdapter extends BaseAdapter{

    private Context mContext;
    private List<EventModel> list_group = new ArrayList<EventModel>();
    private List<EventModel> list_event = new ArrayList<EventModel>();

    public GroupListAdapter(Context context, List<EventModel> list_group, List<EventModel> list_event){
        mContext = context;
        this.list_group = list_group;
        this.list_event = list_event;
    }

    @Override
    public int getCount() {
        return list_event.size();
    }

    @Override
    public EventModel getItem(int position) {
        return list_event.get(position);
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
            convertView = View.inflate(mContext, R.layout.list_event_list_items, null);
            viewHolder.ll_list_items = (LinearLayout) convertView.findViewById(R.id.ll_list_items);
            viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time_left = (TextView) convertView.findViewById(R.id.tv_time_left);
            viewHolder.rl_group_items = (RelativeLayout) convertView.findViewById(R.id.rl_group_items);
            viewHolder.tv_group_date = (TextView) convertView.findViewById(R.id.tv_group_date);
            viewHolder.tv_group_lunar = (TextView) convertView.findViewById(R.id.tv_group_lunar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list_group.contains(getItem(position))){
            viewHolder.ll_list_items.setVisibility(View.GONE);
            viewHolder.rl_group_items.setVisibility(View.VISIBLE);
            viewHolder.tv_group_date.setText(TimeUtils.dateToStrShort(getItem(position).getDate()));
            viewHolder.tv_group_lunar.setText(TimeUtils.dateToStrShort(getItem(position).getDate()));
        } else {
            viewHolder.ll_list_items.setVisibility(View.VISIBLE);
            viewHolder.rl_group_items.setVisibility(View.GONE);
            viewHolder.tv_start_time.setText(TimeUtils.timeToStrShort1(getItem(position).getRemind()));
            viewHolder.tv_end_time.setText(getItem(position).getDate().toString());
            viewHolder.tv_content.setText(getItem(position).getTitle());
            viewHolder.tv_time_left.setText("");
        }

        /*if (convertView == null){
            viewHolder = new ViewHolder();
            if (list_group.contains(getItem(position))){
                convertView = View.inflate(mContext, R.layout.list_event_group_items, null);
                viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_group_date);
                viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_group_lunar);
            }else{
                convertView = View.inflate(mContext, R.layout.list_event_list_items, null);
                viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
                viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
                viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                viewHolder.tv_time_left = (TextView) convertView.findViewById(R.id.tv_time_left);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_start_time.setText(getItem(position).getTime());
        viewHolder.tv_end_time.setText(getItem(position).getTime());
        if(viewHolder.tv_content != null) {
            viewHolder.tv_content.setText(getItem(position).getTitle());
        }
        if(viewHolder.tv_time_left != null) {
            viewHolder.tv_time_left.setText("");
        }*/

        /*viewHolder = new ViewHolder();
        if (list_group.contains(getItem(position))){
            convertView = View.inflate(mContext, R.layout.list_event_group_items, null);
            viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_group_date);
            viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_group_lunar);
        }else{
            convertView = View.inflate(mContext, R.layout.list_event_list_items, null);
            viewHolder.tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            viewHolder.tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time_left = (TextView) convertView.findViewById(R.id.tv_time_left);
        }

        viewHolder.tv_start_time.setText(getItem(position).getDate());
        viewHolder.tv_end_time.setText(getItem(position).getDate());
        if(viewHolder.tv_content != null) {
            viewHolder.tv_content.setText(getItem(position).getTitle());
        }
        if(viewHolder.tv_time_left != null) {
            viewHolder.tv_time_left.setText("");
        }*/

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        for (int i = 0; i < list_group.size(); i++) {
            if(list_group.get(i)==list_event.get(position))
                return false;
        }
        /*if (list_group.contains(list_event.get(position))){
            return false;
        }*/

        return super.isEnabled(position);
    }


    class ViewHolder{
        LinearLayout ll_list_items;
        RelativeLayout rl_group_items;
        TextView tv_start_time, tv_end_time, tv_content, tv_time_left, tv_group_date, tv_group_lunar;
    }
}
