package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.R;
import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.ProductItem;
import com.android.shopping.model.ProductListResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductListRepository {

    private ProductResponseDao mProductResponseDao;
    private Context mContext;

    public ProductListRepository(Context context) {
        mContext = context;
        mProductResponseDao = AppDatabase.getInstance(context).responseDao();
    }

    public Flowable<List<ProductItem>> getProductList() {
        return mProductResponseDao.getListingProducts().map(productItems -> {
            if (productItems == null || productItems.isEmpty()) {
                loadJSONFromAsset();
            }
            return productItems;
        });
    }

    private void loadJSONFromAsset() {
        Completable.fromAction(this::addJsonToFb)
                .subscribeOn(Schedulers.io()).subscribe();
    }



    private void addJsonToFb() {
        String json = null;
        InputStream is;
        try {
            is = mContext.getResources().openRawResource(R.raw.products);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ProductListResponse productListResponse = new Gson().fromJson(json, ProductListResponse.class);

        AppDatabase.getInstance(mContext.getApplicationContext()).responseDao().insertAllProducts(productListResponse.getProducts());

    }


}
