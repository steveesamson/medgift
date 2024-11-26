package com.apollo.medgift.models;

import com.apollo.medgift.common.BaseModel;

public class Service extends BaseModel {
    public static final String STORE = "Service";
    private String serviceName;
    private String serviceAssignee;
    private String hospital;
    private double price;
    private String serviceType;
    private byte[] image;
    private String description;
    private String createdByName;
    private String createdBy;

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceAssignee() {
        return serviceAssignee;
    }

    public void setServiceAssignee(String serviceAssignee) {
        this.serviceAssignee = serviceAssignee;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
