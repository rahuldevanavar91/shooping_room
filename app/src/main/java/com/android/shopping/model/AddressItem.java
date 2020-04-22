package com.android.shopping.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "address")
public class AddressItem {

    @Ignore
    private int viewType;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "pincode")
    private String pinCode;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "landmark")
    private String landmark;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "state")
    private String sate;

    public String getCity() {
        return city;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getSate() {
        return sate;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void setSate(String sate) {
        this.sate = sate;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
