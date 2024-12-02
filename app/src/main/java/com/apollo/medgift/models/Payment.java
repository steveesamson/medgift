package com.apollo.medgift.models;

import java.io.Serializable;

public class Payment implements Serializable {
    public static final String STORE = "Payment";
    private double tax;
    private double orderTotal;

    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}
