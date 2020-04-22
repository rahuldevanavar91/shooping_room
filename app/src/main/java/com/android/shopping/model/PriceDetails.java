package com.android.shopping.model;

public class PriceDetails {


    private int totalPrice;
    private int totalDiscountPrice;
    int shippingCharges;
    int totalPayable;

    public PriceDetails(int totalPrice, int totalDiscountPrice, int shippingCharges, int totalPayable) {
        this.totalPrice = totalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
        this.shippingCharges = shippingCharges;
        this.totalPayable = totalPayable;
    }

    public int getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getShippingCharges() {
        return shippingCharges;
    }

    public int getTotalPayable() {
        return totalPayable;
    }

}
