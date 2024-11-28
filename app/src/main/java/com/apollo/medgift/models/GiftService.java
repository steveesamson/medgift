package com.apollo.medgift.models;


import com.apollo.medgift.common.BaseModel;

public class GiftService extends BaseModel {

    public static final String STORE ="GiftService";

    private String giftId;
    private String gifterEmail;
    private String serviceId;
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
}


