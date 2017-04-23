package com.timemanagement.zxg.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.utils.LogUtils;
import com.timemanagement.zxg.widget.EventDayView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** ViewPager适配器
 * Created by zxg on 2016/11/8.
 * QQ:1092885570
 */

public class ViewPagerAdapter extends PagerAdapter/* implements View.OnClickListener*/{

    private List<EventDayView> vp_views;
    private List<DayDateModel[]> mDayDateModels;

//    private Calendar mCalendar = Calendar.getInstance();
//    private int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

    private int mChildCount = 0;

    public ViewPagerAdapter(List<EventDayView> views, List<DayDateModel[]> dayDateModels) {
        if (views == null){
            vp_views = new ArrayList<EventDayView>();
        } else {
            vp_views = views;
        }

        if (dayDateModels == null){
            mDayDateModels = new ArrayList<>();
        } else {
            mDayDateModels = dayDateModels;
        }
    }

    @Override
    public int getCount() {
        return vp_views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LogUtils.i("instantiateItem1", "Position:"+position);
        container.addView(vp_views.get(position));

        switch (position){
            case 0:
                vp_views.get(position).setItemData(mDayDateModels.get(3));
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                vp_views.get(position).setItemData(mDayDateModels.get(position-1));
                break;
            case 5:
                vp_views.get(position).setItemData(mDayDateModels.get(0));
                break;
        }

        /*DayDateModel _dayDateModel = new DayDateModel();
        for (int i = 0; i < 7; i++) {
            switch (position){
                case 0:
                    _dayDateModel = mDayDateModels.get(3)[i];
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    _dayDateModel = mDayDateModels.get(position-1)[i];
                    break;
                case 5:
                    _dayDateModel = mDayDateModels.get(0)[i];
                    break;
            }

            vp_views.get(position).tv_days[i].setText(_dayDateModel.getDay());

            if (_dayDateModel.getWeek().equals(mWeek+"")) {
                //星期跟当前日期相同的日期，字体颜色设置为白色
                vp_views.get(position).tv_days[i].setTextColor(Color.WHITE);
                //星期跟当前日期相同的日期，设置背景颜色
                if (_dayDateModel.getYear().equals(mCalendar.get(Calendar.YEAR)+"")
                        && _dayDateModel.getMonth().equals(mCalendar.get(Calendar.MONTH)+1+"")
                        && _dayDateModel.getDay().equals(mCalendar.get(Calendar.DAY_OF_MONTH)+"")) {
                    vp_views.get(position).ll_days[i].setBackgroundResource(R.drawable.bg_circular_red);
                } else {
                    vp_views.get(position).ll_days[i].setBackgroundResource(R.drawable.bg_circular_blue);
                }
            }
        }*/
        return vp_views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LogUtils.i("instantiateItem", "Position:"+position);
        container.removeView(vp_views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 解决数据更新，页面不更新
     */
    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object)   {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
