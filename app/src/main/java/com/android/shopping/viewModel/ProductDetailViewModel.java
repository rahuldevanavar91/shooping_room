package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.R;
import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.ProductItem;
import com.android.shopping.util.RequestType;
import com.android.shopping.util.Resource;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductDetailViewModel extends AndroidViewModel {
    private MutableLiveData<Resource<ProductItem>> mResultLiveData;
    private ProductResponseDao mProductResponseDao;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        mResultLiveData = new MutableLiveData<>();
        mProductResponseDao = AppDatabase.getInstance(application.getApplicationContext())
                .responseDao();

    }

    public MutableLiveData<Resource<ProductItem>> getResultLiveData() {
        return mResultLiveData;
    }


    public void getProductDetail(String id) {
        mResultLiveData.setValue(Resource.loading(RequestType.getProductDetail));
        mProductResponseDao.getProductDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableSingleObserver<ProductItem>() {
                    @Override
                    public void onSuccess(ProductItem productItem) {
                        mResultLiveData.setValue(Resource.success(productItem, RequestType.getProductDetail));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResultLiveData.setValue(Resource.error(null, RequestType.getProductDetail));
                    }
                });
    }

    public void checkInCart(String productId) {
        Observable.fromCallable(() -> mProductResponseDao.isInCart(productId))
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        ProductItem productItem = new ProductItem();
                        productItem.setIsInCart(integer);
                        mResultLiveData.setValue(Resource.success(productItem, RequestType.checkInCart));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResultLiveData.setValue(Resource.error(e.getMessage(), RequestType.checkInCart));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void requestForAddToCart(String id) {

        mResultLiveData.setValue(Resource.loading(RequestType.addToCart));
        Observable.fromCallable(() -> mProductResponseDao.insertIntoCart(id, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long integer) {

                        mResultLiveData.setValue(Resource.success(null, RequestType.addToCart));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResultLiveData.setValue(Resource.error(e.getMessage(), RequestType.addToCart));
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }

}
