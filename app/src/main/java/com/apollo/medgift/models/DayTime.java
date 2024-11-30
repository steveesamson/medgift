package com.apollo.medgift.models;


import com.apollo.medgift.common.BaseModel;
import com.apollo.medgift.common.Util;


public class DayTime extends BaseModel {
    public static final String STORE = "DayTime";
    private String day;
    private String startTime;
    private String endTime;

    public DayTime(){
    }
    public DayTime(Day day){
        this.day = day.name();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String d) {
        day = d;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String toString(){
        return String.format("day:%s, start:%s, end:%s", day, startTime, endTime);
    }
}
