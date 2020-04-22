package com.android.shopping.model.converter;

import androidx.room.TypeConverter;

import com.android.shopping.model.PriceDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PriceTyepConverter {
    @TypeConverter
    public static PriceDetails fromString(String value) {
        Type listType = new TypeToken<PriceDetails>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(PriceDetails list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
