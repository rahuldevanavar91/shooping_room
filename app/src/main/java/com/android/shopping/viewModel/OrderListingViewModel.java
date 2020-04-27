package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.ProductItem;
import com.android.shopping.network.Resource;
import com.android.shopping.ui.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class OrderListingViewModel extends AndroidViewModel {

    private ProductResponseDao mDao;
    private MutableLiveData<Resource<List<ProductItem>>> mResult;

    public OrderListingViewModel(@NonNull Application application) {
        super(application);
        mDao = AppDatabase.getInstance(application.getApplicationContext())
                .responseDao();
        mResult = new MutableLiveData<>();
        getOrderList();
    }

    public MutableLiveData<Resource<List<ProductItem>>> getResult() {
        return mResult;
    }

    public void getOrderList() {
        mDao.getOrderDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableSubscriber<List<OrderDetails>>() {
                    @Override
                    public void onNext(List<OrderDetails> list) {
                        List<ProductItem> productItems = new ArrayList<>();
                        for (OrderDetails orderDetails : list) {
                            ProductItem productItem = new ProductItem();
                            productItem.setViewType(ProductListAdapter.VIEW_TYPE_ORDER_PLACED);
                            productItem.setOrderItem(orderDetails);
                            productItems.add(productItem);
                        }
                        mResult.setValue(Resource.success(productItems));

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
