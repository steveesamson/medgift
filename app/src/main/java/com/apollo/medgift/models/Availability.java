package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class Availability extends BaseModel {
    public static  final  String STORE = "Availability";

    private String createdBy;
    private List<DayTime> times = new ArrayList<>();

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<DayTime> getTimes() {
        return times;
    }

    public void setTimes(List<DayTime> times) {
        this.times = times;
    }
}
