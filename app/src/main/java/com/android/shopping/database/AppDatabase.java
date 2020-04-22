package com.android.shopping.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.shopping.model.AddressItem;
import com.android.shopping.model.CartItem;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.ProductItem;


@Database(entities = {ProductItem.class, CartItem.class, AddressItem.class, OrderDetails.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_data_base";
    private static AppDatabase appDatabase;

    public abstract ProductResponseDao responseDao();


    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration().build();
        }
        return appDatabase;
    }
}
