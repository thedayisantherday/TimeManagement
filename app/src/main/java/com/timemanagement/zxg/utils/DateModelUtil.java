package com.timemanagement.zxg.utils;

import com.timemanagement.zxg.model.DayDateModel;
import com.timemanagement.zxg.model.EventModel;
import com.timemanagement.zxg.model.MonthDateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zxg on 17/4/11.
 */

public class DateModelUtil {

    // 两个有序list合并成一个有序list
    public static List<EventModel> sort(List<EventModel> list1 , List<EventModel> list2){
        List<EventModel> list = new ArrayList<EventModel>();
        int i = 0 , j = 0;
        while(i < list1.size() && j < list2.size()){
            Date remind1 = list1.get(i).getRemind();
            Date remind2 = list2.get(j).getRemind();
            if(remind1.getHours()<remind2.getHours() ||
                    (remind1.getHours()==remind2.getHours() && remind1.getMinutes()<=remind2.getMinutes())){
                list.add(list1.get(i++));
            }else{
                list.add(list2.get(j++));
            }
        }
        while(i < list1.size()){
            list.add(list1.get(i++));
        }
        while(j < list2.size()){
            list.add(list2.get(j++));
        }
        return list;
    }

    public static DayDateModel[] getWeekDayDateModels(Calendar calendar){
        LogUtils.i("getWeekDayDateModels", "year:"+calendar.get(Calendar.YEAR)+
                ", month:"+calendar.get(Calendar.MONTH)+", day:"+calendar.get(Calendar.DAY_OF_MONTH));

        DayDateModel[] dayDateModels = new DayDateModel[7];
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        int iWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < 7; i++) {
            dayDateModels[i] = new DayDateModel();

            LogUtils.i("getDayDateModels", "iWeek:"+iWeek+",i:"+i+", i-iWeek:"+(i-iWeek));
            LogUtils.i("getDayDateModels", "year:"+calendar.get(Calendar.YEAR)+
                    ", month:"+calendar.get(Calendar.MONTH)+", day:"+calendar.get(Calendar.DAY_OF_MONTH));
            Calendar _calendar = TimeUtils.standardDate(calendar, i+1-iWeek);
            dayDateModels[i].setYear(_calendar.get(Calendar.YEAR)+"");
            dayDateModels[i].setMonth(_calendar.get(Calendar.MONTH)+1+"");
            dayDateModels[i].setDay(_calendar.get(Calendar.DAY_OF_MONTH)+"");
            dayDateModels[i].setWeek(_calendar.get(Calendar.DAY_OF_WEEK)+"");
            if (_calendar.get(Calendar.YEAR) >= 1900 && _calendar.get(Calendar.YEAR) <= 2100)
            dayDateModels[i].setLunar(new LunarUtils(_calendar).toString());
        }
        return dayDateModels;
    }

    public static MonthDateModel getMonthDateModel(int year, int month) {
        return getMonthDateModel(year, month, true);
    }

    public static MonthDateModel getMonthDateModel(int year, int month, boolean isLunar){
        if (year<0 || month<0 || month>12){
            return null;
        }
        MonthDateModel monthDateModel = new MonthDateModel();
        monthDateModel.setYear(String.valueOf(year));
        monthDateModel.setMonth(String.valueOf(month));

        List<DayDateModel> listDay =  new ArrayList<DayDateModel>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int j = 1; j <= maxDays; j++) {
            DayDateModel dayDateModel = new DayDateModel();
            dayDateModel.setYear(String.valueOf(year));
            dayDateModel.setMonth(String.valueOf(month));
            dayDateModel.setDay(String.valueOf(j));

            Calendar cal = Calendar.getInstance();
            Date date = new Date(year-1900, month-1, j);
            cal.setTime(date);
            int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            dayDateModel.setWeek(String.valueOf(week_index));
            if (isLunar && year >= 1900 && year < 2050) {
                LunarUtils lunarUtils = new LunarUtils(cal);
                dayDateModel.setLunar(lunarUtils.get_month() + lunarUtils.get_date());
            }
            listDay.add(dayDateModel);
        }
        monthDateModel.setDayDateModels(listDay);
        return monthDateModel;
    }

    /**
     * 将传入的年月标准化为合法年月
     * @param year
     * @param month
     * @return
     */
    public static int[] getStandardMonth(int year, int month){
        LogUtils.i("getStandardMonth", "year:"+year+", month:"+month);
        if (month<=0){
            year = year + month/12 -1;
            month = month%12 + 12;
        }else if (month > 12){
            year = year + month/12;
            month = month%12;
        }
        if (year <= 0){
            year = Math.abs(year)+1;
        }
        LogUtils.i("getMonth", "year:"+year+", month:"+month);
        return new int[]{year, month};
    }
}
