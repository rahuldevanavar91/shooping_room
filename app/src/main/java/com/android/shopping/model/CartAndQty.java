package com.android.shopping.model;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class CartAndQty {
    @Ignore
    private int viewType;

    private int qty;
    private int cart_id;

    @Embedded
    private ProductItem productItem;

    @Ignore
    private PriceDetails priceDetails;


    public int getQty() {
        return qty;

    }

    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem product_list) {
        this.productItem = product_list;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getViewType() {
        return viewType;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

}
