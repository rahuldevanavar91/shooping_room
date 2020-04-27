package com.android.shopping.model;

import java.util.List;

public class ProductResponse {
    private List<ProductItem> products;

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }
}
