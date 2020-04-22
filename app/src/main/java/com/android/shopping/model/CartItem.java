package com.android.shopping.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart",
        foreignKeys = @ForeignKey(entity = ProductItem.class,
                parentColumns = "product_id", childColumns = "id"))
public class CartItem {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "cart_id")
    private int cartId;

    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "qty")
    private int qty;

    public int getId() {
        return id;
    }

    public int getCartId() {
        return cartId;
    }

    public int getQty() {
        return qty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
