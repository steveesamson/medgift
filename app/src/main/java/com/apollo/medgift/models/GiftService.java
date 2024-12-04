package com.apollo.medgift.models;


import android.icu.util.Calendar;

import androidx.annotation.NonNull;

import com.apollo.medgift.common.BaseModel;
import com.apollo.medgift.common.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GiftService extends BaseModel {

    public static final String STORE ="GiftService";

    private String giftId;
    private String giftOwner;
    private String serviceId;
    private String serviceOwner;
    private String gifterEmail;
    private String gifterName;
    private String serviceName;
    private String serviceDescription;
    private double servicePrice;
    private String status = ServiceStatus.SCHEDULED;
    private String contributionDate;
    private String deliveryDate;
    private String confirmationDate;
    private String recipientName;

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGiftOwner() {
        return giftOwner;
    }

    public void setGiftOwner(String giftOwner) {
        this.giftOwner = giftOwner;
    }

    public String getServiceOwner() {
        return serviceOwner;
    }

    public void setServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
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

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    @NonNull
    @Override
    public String toString(){
        return gifterName;
    }


    // Parse input to date and time object
    private Calendar getDate(){
        Calendar cal = Calendar.getInstance();
        Date d = Util.parseTime(this.deliveryDate);
        cal.setTime(d);
        return cal;
    }


    // Date formater method
    private String formatedDueTime(Date date){
        String DATE_PATTERN = "HH:mm. MMM dd, yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    // Calculate interval for task
    public long dueIntervalInMillis(){
        return Math.abs(getDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
    }

    // Calculate interval for watch
    public long watchIntervalInMillis(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDate().getTime());
        cal.add(Calendar.HOUR, - 3);
        return Math.abs(cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
    }

    // Check validity of task
    public boolean isPending(){
        return getDate().getTimeInMillis() > Calendar.getInstance().getTimeInMillis();
    }

    // Check validity of task for watch
    public boolean isPendingWatch(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 3);
        return cal.getTimeInMillis() < getDate().getTimeInMillis() ;
    }
}


