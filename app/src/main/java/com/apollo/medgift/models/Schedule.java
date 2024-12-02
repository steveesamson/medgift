package com.apollo.medgift.models;

public class Schedule {
    private String schedule;
    private boolean check;

    public String toAMPM(){
        String[] date_time = schedule.split(" ");
        DateTimeValue dtv = new DateTimeValue(date_time[1]);
        return dtv.toAMPM();
    }

    public  Schedule(String schedule){
        this.schedule = schedule;

    }
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
