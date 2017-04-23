package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.timemanagement.zxg.activities.activitycontrol.BaseActivity;
import com.timemanagement.zxg.adapter.GroupListAdapter;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.timemanagement.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 17/2/7.
 */

public class EventListFragment extends Fragment {

    private Activity mActivity;
    private ListView lv_event_list;

    private List<EventModel> list=null;
    private List<EventModel> groupkey=new ArrayList<EventModel>();
    private List<EventModel> aList = new ArrayList<EventModel>();
    private List<EventModel> bList = new ArrayList<EventModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        initView();
        initData();
    }

    private void initView(){
        lv_event_list = (ListView) mActivity.findViewById(R.id.lv_event_list);
        ((BaseActivity)mActivity).tv_left.setVisibility(View.GONE);
    }

    private void initData(){
        list = new ArrayList<EventModel>();

        EventModel model = new EventModel();
        model.setDate(new Date(2016, 03, 15));
        groupkey.add(model);
        model = new EventModel();
        model.setDate(new Date(2016, 03, 15));
        groupkey.add(model);

        for(int i=0; i<2; i++){
            model = new EventModel();
            model.setDate(new Date(2016, 03, 15));
            model.setTitle("平安");
            aList.add(model);
        }
        list.add(groupkey.get(0));
        list.addAll(aList);

        for(int i=0; i<15; i++){
            model = new EventModel();
            model.setDate(new Date(2016, 03, 15));
            model.setTitle("科技");
            bList.add(model);
        }
        list.add(groupkey.get(1));
        list.addAll(bList);

        GroupListAdapter groupListAdapter = new GroupListAdapter(mActivity, groupkey, list);
        lv_event_list.setAdapter(groupListAdapter);
    }
}
