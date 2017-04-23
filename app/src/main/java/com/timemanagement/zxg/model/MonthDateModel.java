package com.timemanagement.zxg.model;

import java.util.List;

/**
 * Created by zxg on 17/2/13.
 */

public class MonthDateModel {
    
    private String year;
    private String month;
    private List<DayDateModel> dayDateModels;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<DayDateModel> getDayDateModels() {
        return dayDateModels;
    }

    public void setDayDateModels(List<DayDateModel> dayDateModels) {
        this.dayDateModels = dayDateModels;
    }

}
