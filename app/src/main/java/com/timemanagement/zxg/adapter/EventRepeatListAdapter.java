package com.timemanagement.zxg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 17/3/5.
 */

public class EventRepeatListAdapter extends BaseAdapter {

    private Context mContext;
    private List<EventModel> mEventModels = new ArrayList<>();

    public EventRepeatListAdapter(Context context, List<EventModel> eventModels) {
        mContext = context;
        mEventModels = eventModels;
    }

    @Override
    public int getCount() {
        return mEventModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();

            convertView = View.inflate(mContext, R.layout.layout_event_repeat_list, null);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.tv_week_date = (TextView) convertView.findViewById(R.id.tv_week_date);
            viewHolder.tv_lunar_date = (TextView) convertView.findViewById(R.id.tv_lunar_date);
            viewHolder.lv_event_list_date = (ListView) convertView.findViewById(R.id.lv_event_list_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.lv_event_list_date.setAdapter(new GroupListAdapter(mContext, null, mEventModels));
        return convertView;
    }

    class ViewHolder{
        TextView tv_date, tv_week_date, tv_lunar_date;
        ListView lv_event_list_date;
    }
}
