package com.timemanagement.zxg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.GroupListAdapter;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventListActivity extends BaseActivity {

    private Context mContext;
    private ListView lv_event_list;

    private List<EventModel> list=null;
    private List<EventModel> groupkey=new ArrayList<EventModel>();
    private List<EventModel> aList = new ArrayList<EventModel>();
    private List<EventModel> bList = new ArrayList<EventModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_event_list);

        mContext = this;

        lv_event_list = (ListView) findViewById(R.id.lv_event_list);
        initData();
        lv_event_list.setAdapter(new GroupListAdapter(mthis, groupkey, list));
//        lv_event_list.setAdapter(new EventRepeatListAdapter(mContext));

    }

    private void initData(){
        list = new ArrayList<EventModel>();

        EventModel model = new EventModel();
        model.setDate(new Date(2016, 03, 15));
        groupkey.add(model);
        model = new EventModel();
        model.setDate(new Date(2017, 03, 15));
        groupkey.add(model);

        for(int i=0; i<2; i++){
            model = new EventModel();
            model.setDate(new Date(2017, 02, 14));
            model.setTitle("平安");
            aList.add(model);
        }
        list.add(groupkey.get(0));
        list.addAll(aList);

        for(int i=0; i<15; i++){
            model = new EventModel();
            model.setDate(new Date(2017, 01, 01));
            model.setTitle("科技");
            bList.add(model);
        }
        list.add(groupkey.get(1));
        list.addAll(bList);

        GroupListAdapter groupListAdapter = new GroupListAdapter(mthis, groupkey, list);
        lv_event_list.setAdapter(groupListAdapter);
    }

    @Override
    public void initHead(ViewHolder viewHolder) {

    }

    public static void startSelf(Context context){
        Intent intent = new Intent(context, EventListActivity.class);
        context.startActivity(intent);
    }
}
