package com.timemanagement.zxg.model;

import java.io.Serializable;

/**
 * Created by zxg on 17/2/13.
 */

public class DayDateModel implements Serializable {

    private String year;
    private String month;
    private String day;
    private String week;
    private String lunar;

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLunar() {
        return lunar;
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }
}
