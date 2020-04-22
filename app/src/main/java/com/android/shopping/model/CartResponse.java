package com.android.shopping.model;

import java.util.List;

public class CartResponse {

    private List<CartAndQty> productItem;
    private PriceDetails priceDetails;

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

    public void setProductItem(List<CartAndQty> productItem) {
        this.productItem = productItem;
    }

    public List<CartAndQty> getProductItem() {
        return productItem;
    }
}
