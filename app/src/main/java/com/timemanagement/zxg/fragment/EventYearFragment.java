package com.timemanagement.zxg.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timemanagement.zxg.activities.MainActivity;
import com.timemanagement.zxg.adapter.EventYearAdapter;
import com.timemanagement.zxg.timemanagement.R;

import java.util.Calendar;

/**
 * Created by zxg on 17/2/22.
 */

public class EventYearFragment extends Fragment {

    private Activity mActivity;
    private RecyclerView rv_year;
    private LinearLayoutManager recyclerLayoutManagement;
    private EventYearAdapter eventYearAdapter;
    private Calendar mCalendar = Calendar.getInstance();

    private boolean isCurrent = false;
    private int mYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_year, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity = getActivity();

        initView();
        initData();
    }

    private void initView () {
        recyclerLayoutManagement = new LinearLayoutManager(mActivity);
        rv_year = (RecyclerView) mActivity.findViewById(R.id.rv_year);
        //使RecyclerView保持固定的大小，用于自身的优化
        rv_year.setHasFixedSize(true);
        rv_year.setLayoutManager(recyclerLayoutManagement);
        ((MainActivity)mActivity).setTopLefText("", View.GONE);
    }

    private void initData(){
        Bundle arguments = getArguments();
        if (arguments != null) {
            mYear = arguments.getInt("year", mCalendar.get(Calendar.YEAR));
        }
        if (mYear<=0){
            mYear = mCalendar.get(Calendar.YEAR);
        }
//        eventYearAdapter = new EventYearAdapter(mActivity, mYearDateModels);
        eventYearAdapter = new EventYearAdapter(mActivity);
        rv_year.setAdapter(eventYearAdapter);
        rv_year.setOnScrollListener(new RecyclerViewOnScrollListener());
    }

    @Override
    public void onStart() {
        super.onStart();
        rv_year.scrollToPosition(mYear-1);
    }

    class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = recyclerLayoutManagement.findFirstVisibleItemPosition();
            int currentYear = mCalendar.get(Calendar.YEAR);
            isCurrent = (currentYear == firstVisibleItem) || (currentYear == firstVisibleItem+1);
        }
    }

    public void gotoToday(){
        if (!isCurrent) {
            rv_year.scrollToPosition(mCalendar.get(Calendar.YEAR) - 1);
        } else {
            ((MainActivity)mActivity).setFragment(-1, null);
        }
    }
}
