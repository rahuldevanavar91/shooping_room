package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.AddressItem;
import com.android.shopping.ui.adapter.AddressListAdapter;
import com.android.shopping.util.RequestType;
import com.android.shopping.util.Resource;

import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddressViewModel extends AndroidViewModel {
    private ProductResponseDao dao;
    private MutableLiveData<Resource<List<AddressItem>>> mAddressLiveData;

    public AddressViewModel(@NonNull Application application) {
        super(application);
        dao = AppDatabase.getInstance(application.getApplicationContext()).responseDao();
        mAddressLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<Resource<List<AddressItem>>> getAddressLiveData() {
        return mAddressLiveData;
    }

    public void getAddress() {
        dao.getAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableSingleObserver<List<AddressItem>>() {
                    @Override
                    public void onSuccess(List<AddressItem> addressItems) {
                        if (addressItems != null && !addressItems.isEmpty()) {
                            AddressItem addAddress = new AddressItem();
                            addAddress.setViewType(AddressListAdapter.VIEW_TYPE_ADD_ADDRESS);
                            addressItems.add(0, addAddress);
                            mAddressLiveData.setValue(Resource.success(addressItems, RequestType.getAddresss));
                        } else {
                            mAddressLiveData.setValue(Resource.success(null, RequestType.getAddresss));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAddressLiveData.setValue(Resource.error(e.getMessage(), RequestType.getAddresss));
                    }
                });
    }


    public void saveAddress(AddressItem item) {
        Observable.fromCallable(() -> dao.saveAddress(item))
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long integer) {
                        mAddressLiveData.setValue(Resource.success(null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mAddressLiveData.setValue(Resource.error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
