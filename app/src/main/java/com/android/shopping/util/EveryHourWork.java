package com.android.shopping.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.model.ProductItem;

public class EveryHourWork extends Worker {
    private static final String TAG = "work";

    public EveryHourWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "dork");
        ProductItem productItem = AppDatabase.getInstance(getApplicationContext())
                .responseDao().getRandomProduct();
        if (productItem != null) {
            NotificationUtils.showNotificationMessage(getApplicationContext(), productItem);
        }
        return Result.success();
    }
}
