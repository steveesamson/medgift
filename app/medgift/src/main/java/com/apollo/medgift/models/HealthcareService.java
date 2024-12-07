package com.apollo.medgift.models;

import android.net.Uri;

import com.apollo.medgift.common.BaseModel;

public class HealthcareService extends BaseModel {
    public static final String STORE = "HealthcareService";

    // Not to be persisted
    public transient Uri bannerUri = null;
    private String serviceName;
    private String description;
    private String bannerUrl;
    private String createdBy;
    private String serviceType;
    private double price;
    private int ratings;



    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
