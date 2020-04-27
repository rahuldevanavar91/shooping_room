package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.model.ProductItem;
import com.android.shopping.network.RequestType;
import com.android.shopping.network.Resource;
import com.android.shopping.repository.ProductListRepository;

import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;


public class ProductLisViewModel extends AndroidViewModel {

    private MutableLiveData<Resource<List<ProductItem>>> mProductListResult;
    private ProductListRepository mProductListRepository;
    private CompositeDisposable compositeDisposable;



    public ProductLisViewModel(@NonNull Application application) {
        super(application);
        mProductListResult = new MutableLiveData<>();
        mProductListRepository = new ProductListRepository(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        getProductList();
    }

    public MutableLiveData<Resource<List<ProductItem>>> getProductListResult() {
        return mProductListResult;
    }

    public void cancelOrder(int parentPosition, int orderId) {

        mProductListRepository.cancelOrder(parentPosition, orderId).observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new Observer<List<ProductItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ProductItem> productItems) {
                        mProductListResult.setValue(Resource.success(productItems, RequestType.cancelOrder, parentPosition));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProductListResult.setValue(Resource.error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getProductList() {
        mProductListResult.setValue(Resource.loading());
        compositeDisposable.add(mProductListRepository.getProductList().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableSubscriber<List<ProductItem>>() {
                    @Override
                    public void onNext(List<ProductItem> productItems) {
                        if (productItems != null) {
                            mProductListResult.setValue(Resource.success(productItems, RequestType.productList));
                        } else {
                            mProductListResult.setValue(Resource.error("No product found"));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        mProductListResult.setValue(Resource.error(t.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
