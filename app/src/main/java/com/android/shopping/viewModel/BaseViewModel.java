package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.repository.BaseRepository;
import com.android.shopping.util.Resource;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class BaseViewModel extends AndroidViewModel {
    private MutableLiveData<Resource<String>> mData;
    private BaseRepository mBaseRepo;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mData = new MutableLiveData<>();
        mBaseRepo = BaseRepository.getBaseRepoInstance(application.getApplicationContext());
    }

    public MutableLiveData<Resource<String>> getData() {
        return mData;
    }

    public void getCartCount() {
        mBaseRepo.getCartCount().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        mData.setValue(Resource.success(String.valueOf(integer)));
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
