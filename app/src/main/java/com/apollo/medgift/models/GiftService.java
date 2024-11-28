package com.apollo.medgift.models;


import androidx.annotation.NonNull;

import com.apollo.medgift.common.BaseModel;

public class GiftService extends BaseModel {

    public static final String STORE ="GiftService";

    private String giftId;
    private String serviceId;
    private String gifterEmail;
    private String gifterName;
    private String serviceName;
    private String serviceDescription;
    private double servicePrice;

    private String status = ServiceStatus.SCHEDULED;
    private String contributionDate;
    private String deliveryDate;
    private String confirmationDate;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGifterEmail() {
        return gifterEmail;
    }

    public void setGifterEmail(String gifterEmail) {
        this.gifterEmail = gifterEmail;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(String contributionDate) {
        this.contributionDate = contributionDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(String confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getGifterName() {
        return gifterName;
    }

    public void setGifterName(String gifterName) {
        this.gifterName = gifterName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(double servicePrice) {
        this.servicePrice = servicePrice;
    }

    @NonNull
    @Override
    public String toString(){
        return gifterName;
//        return String.format("%s <%s>", gifterName, gifterEmail);
    }
}


