package com.android.shopping.model.converter;

import androidx.room.TypeConverter;

import com.android.shopping.model.CartAndQty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CartAndQtyListConverter {
    @TypeConverter
    public static List<CartAndQty> fromString(String value) {
        Type listType = new TypeToken<List<CartAndQty>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<CartAndQty> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
