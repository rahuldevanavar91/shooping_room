package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.model.PriceDetails;
import com.android.shopping.repository.PaymentRepository;
import com.android.shopping.util.Resource;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class PaymentViewModel extends AndroidViewModel {
    private MutableLiveData<Resource<PriceDetails>> mResourceMutableLiveData;
    private PaymentRepository mPaymentRepository;
    private CompositeDisposable compositeDisposable;

    public PaymentViewModel(@NonNull Application application) {
        super(application);
        mResourceMutableLiveData = new MutableLiveData<>();
        mPaymentRepository = new PaymentRepository(getApplication().getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        getPaymentsDetails();
    }

    private void getPaymentsDetails() {
        compositeDisposable.add(RxJavaBridge.toV2Disposable(mPaymentRepository.getPaymentDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableSubscriber<PriceDetails>() {
                    @Override
                    public void onNext(PriceDetails priceDetails) {
                        mResourceMutableLiveData.setValue(Resource.success(priceDetails));
                    }

                    @Override
                    public void onError(Throwable t) {
                        mResourceMutableLiveData.setValue(Resource.error(t.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                })));

    }

    public MutableLiveData<Resource<PriceDetails>> getResourceMutableLiveData() {
        return mResourceMutableLiveData;
    }

    public void createOrder() {
        compositeDisposable.add(mPaymentRepository.createOrder().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mResourceMutableLiveData.setValue(Resource.error(e.getMessage()));
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
