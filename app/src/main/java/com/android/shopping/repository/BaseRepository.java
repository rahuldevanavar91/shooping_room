package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Flowable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BaseRepository {
    private ProductResponseDao mDao;
    private static BaseRepository mBaseRepoInstance;


    public static BaseRepository getBaseRepoInstance(Context context) {
        if (mBaseRepoInstance == null) {
            mBaseRepoInstance = new BaseRepository(context);
        }
        return mBaseRepoInstance;
    }

    private BaseRepository(Context context) {
        mDao = AppDatabase.getInstance(context).responseDao();
    }

    public Flowable<Integer> getCartCount() {
        return mDao.getCartCount().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .map(integers -> {
                    Integer count = 0;
                    for (Integer integer : integers) {
                        count += integer;
                    }
                    return count;
                });
    }
}
