package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.util.Resource;

import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderListingViewModel extends AndroidViewModel {

    private ProductResponseDao mDao;
    private MutableLiveData<Resource<List<OrderDetails>>> mResult;

    public OrderListingViewModel(@NonNull Application application) {
        super(application);
        mDao = AppDatabase.getInstance(application.getApplicationContext())
                .responseDao();
        mResult = new MutableLiveData<>();
        getOrderList();
    }

    public MutableLiveData<Resource<List<OrderDetails>>> getResult() {
        return mResult;
    }

    public void getOrderList() {
        mDao.getOrderDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableSingleObserver<List<OrderDetails>>() {
                    @Override
                    public void onSuccess(List<OrderDetails> list) {
                        mResult.setValue(Resource.success(list));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mResult.setValue(Resource.error(e.getMessage()));

                    }
                });
    }
}
