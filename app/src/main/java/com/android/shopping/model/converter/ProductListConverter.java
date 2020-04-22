package com.android.shopping.model.converter;

import androidx.room.TypeConverter;

import com.android.shopping.model.ProductItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProductListConverter {
    @TypeConverter
    public static List<ProductItem> fromString(String value) {
        Type listType = new TypeToken<List<ProductItem>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(List<ProductItem> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
