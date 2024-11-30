package com.apollo.medgift.models;

import com.apollo.medgift.common.Util;

public class DateTimeValue {
    public int year;
    public int month;
    public int dayOfMonth;
    public int hourOfDay;
    public int minute;

    public DateTimeValue(){}

    public DateTimeValue(String dateString){
        if(Util.isNullOrEmpty(dateString)){
            return;
        }
        String[] hourMin = dateString.split(":");
        hourOfDay = Integer.parseInt(hourMin[0],10);
        minute = Integer.parseInt(hourMin[1],10);
    }
    private String AM = "AM";
    public String toAMPM(){
        if(hourOfDay == 0){
            return "";
        }
        if(hourOfDay > 12){
            AM = "PM";
        }
        return String.format("%s:%s %s", hourOfDay % 12, minute, AM);
    }

    public String to24Hr(){
        return String.format("%s:%s", hourOfDay, minute);
    }
}
