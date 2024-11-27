package com.apollo.medgift.models;

import androidx.annotation.NonNull;

public class Availability
{

    private String day;
    private String start;
    private String end;
   public Availability(String day) {
       this.day = day;
   }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @NonNull
    @Override
    public String toString(){
       return String.format("%s|%s|%s", this.day, this.start, this.end);
    }
}
