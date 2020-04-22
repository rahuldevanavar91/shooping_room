package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.model.ProductItem;
import com.android.shopping.repository.ProductListRepository;
import com.android.shopping.util.RequestType;
import com.android.shopping.util.Resource;

import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class ProductLisViewModel extends AndroidViewModel {

    private MutableLiveData<Resource<List<ProductItem>>> mProductListResult;
    private ProductListRepository mProductListRepository;
    private CompositeDisposable mCompositeDisposable;

    public ProductLisViewModel(@NonNull Application application) {
        super(application);
        mProductListResult = new MutableLiveData<>();
        mProductListRepository = new ProductListRepository(application.getApplicationContext());
        mCompositeDisposable = new CompositeDisposable();
        getProductList();

    }

    public MutableLiveData<Resource<List<ProductItem>>> getProductListResult() {
        return mProductListResult;
    }

    private void getProductList() {
        mCompositeDisposable.add(mProductListRepository.getProductList().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableSubscriber<List<ProductItem>>() {

                    @Override
                    public void onNext(List<ProductItem> productItems) {
                        if (productItems != null) {
                            mProductListResult.setValue(Resource.success(productItems, RequestType.productList));
                        } else {
                            mProductListResult.setValue(Resource.loading());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProductListResult.setValue(Resource.error(null, RequestType.productList));
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }
}
