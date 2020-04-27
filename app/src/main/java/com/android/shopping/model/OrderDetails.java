package com.android.shopping.model;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.android.shopping.model.converter.PriceTyepConverter;
import com.android.shopping.model.converter.CartAndQtyListConverter;

import java.util.List;

@Entity(tableName = "order_details")
public class OrderDetails {

    @Ignore
    private int viewType;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private int id;

    @ColumnInfo(name = "product_items")
    @TypeConverters(CartAndQtyListConverter.class)
    private List<CartAndQty> productList;


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

    public OrderDetails(List<CartAndQty> cartDetails, String time, PriceDetails priceDetails) {
        this.productList = cartDetails;
        this.date = time;
        this.priceDetails = priceDetails;
    }


    public List<CartAndQty> getProductList() {
        return productList;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setProductList(List<CartAndQty> productItem) {
        this.productList = productItem;
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

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof OrderDetails) {
            return id == ((OrderDetails) obj).id;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 17 * 7 + this.id;
    }
}
