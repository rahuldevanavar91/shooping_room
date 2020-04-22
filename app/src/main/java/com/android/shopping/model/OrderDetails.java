package com.android.shopping.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.android.shopping.model.converter.ProductListConverter;
import com.android.shopping.model.converter.PriceTyepConverter;

import java.util.List;

@Entity(tableName = "order_details")
public class OrderDetails {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private int id;

    @ColumnInfo(name = "product_items")
    @TypeConverters(ProductListConverter.class)
    private List<ProductItem> productItem;


    @ColumnInfo(name = "order_place_date")
    private String date;

    @ColumnInfo(name = "price_details")
    @TypeConverters(PriceTyepConverter.class)
    private PriceDetails priceDetails;

    public int getId() {
        return id;
    }

    public OrderDetails() {

    }

    public OrderDetails(List<ProductItem> productItem, String time, PriceDetails priceDetails) {
        this.productItem = productItem;
        this.date = time;
        this.priceDetails = priceDetails;
    }


    public List<ProductItem> getProductItem() {
        return productItem;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setProductItem(List<ProductItem> productItem) {
        this.productItem = productItem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

}
