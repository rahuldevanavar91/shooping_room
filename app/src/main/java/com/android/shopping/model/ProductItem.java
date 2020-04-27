package com.android.shopping.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.android.shopping.model.converter.StringListConverter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "product_list")
public class ProductItem {

    public ProductItem() {

    }

    @ColumnInfo(name = "view_type")
    private int viewType;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "desc")
    private String desc;

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    @SerializedName("product_id")
    private String productId;


    @TypeConverters(StringListConverter.class)
    @ColumnInfo(name = "image")
    private List<String> image;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "offer_price")
    @SerializedName("offer_price")
    private int offerPrice;

    @ColumnInfo(name = "aboutBrand")
    @SerializedName("about_brand")
    private String aboutBrand;

    @ColumnInfo(name = "thumb_image")
    @SerializedName("thumb_image")
    private String thumbImage;

    @ColumnInfo(name = "offer_label")
    @SerializedName("offer_label")
    private String offerLabel;

    @Ignore
    private int isInCart;
    @Ignore
    private int qty;

    @Ignore
    private OrderDetails orderItem;

    public List<String> getImage() {
        return image;
    }

    public String getAboutBrand() {
        return aboutBrand;
    }

    public String getDesc() {
        return desc;
    }


    public String getName() {
        return name;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setAboutBrand(String aboutBrand) {
        this.aboutBrand = aboutBrand;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(String thumbImage) {
        this.thumbImage = thumbImage;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getOfferLabel() {
        return offerLabel;
    }

    public void setOfferLabel(String offerLabel) {
        this.offerLabel = offerLabel;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(int isInCart) {
        this.isInCart = isInCart;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int hashCode() {
        return 17 * 7 + this.orderItem.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof ProductItem) {
            ProductItem item = (ProductItem) obj;
            if (item.orderItem != null) {
                return this.orderItem.getId() == item.orderItem.getId();
            }
        }
        return super.equals(obj);
    }

    public OrderDetails getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderDetails orderItem) {
        this.orderItem = orderItem;
    }
}
