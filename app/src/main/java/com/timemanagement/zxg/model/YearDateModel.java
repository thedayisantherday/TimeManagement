package com.timemanagement.zxg.model;

import java.util.List;

/**
 * Created by zxg on 17/2/13.
 */

public class YearDateModel {
    
    private String year;
    private String lunarYear;

    public String getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(String lunarYear) {
        this.lunarYear = lunarYear;
    }

    private List<MonthDateModel> monthDateModels;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<MonthDateModel> getMonthDateModels() {
        return monthDateModels;
    }

    public void setMonthDateModels(List<MonthDateModel> monthDateModels) {
        this.monthDateModels = monthDateModels;
    }

}
