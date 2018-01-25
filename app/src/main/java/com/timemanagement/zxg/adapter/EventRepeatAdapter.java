package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;
import com.timemanagement.zxg.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuxiaoguang on 2018/1/25.
 */

public class EventRepeatAdapter extends RecyclerView.Adapter<EventRepeatAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<EventModel> list_repeat_events = new ArrayList<EventModel>();

    public EventRepeatAdapter(Context context, List<EventModel> list_repeat_events) {
        mContext = context;
        this.list_repeat_events = list_repeat_events;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_event_repeat_items, viewGroup, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int i) {
        EventModel eventModel = list_repeat_events.get(i);
        recyclerViewHolder.tv_start_time.setText(TimeUtils.dateToStrShort(eventModel.getRemind()));
        if (eventModel.getRepeatEnd() == null) {
            recyclerViewHolder.tv_end_time.setText("+∞");
        } else {
            recyclerViewHolder.tv_end_time.setText(TimeUtils.dateToStrShort(eventModel.getRepeatEnd()));
        }
        recyclerViewHolder.tv_content.setText(eventModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return list_repeat_events.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_start_time, tv_end_time, tv_content;

        public RecyclerViewHolder(View convertView) {
            super(convertView);
            tv_start_time = (TextView) convertView.findViewById(R.id.tv_start_time);
            tv_end_time = (TextView) convertView.findViewById(R.id.tv_end_time);
            tv_content = (TextView) convertView.findViewById(R.id.tv_content);
        }
    }
}
