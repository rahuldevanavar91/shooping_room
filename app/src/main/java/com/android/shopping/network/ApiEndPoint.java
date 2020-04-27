package com.android.shopping.network;


import com.android.shopping.model.ProductResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;


public interface ApiEndPoint {

    @GET("10821d0a1d5e11913a9a")
    Flowable<ProductResponse> getProducts();
}
