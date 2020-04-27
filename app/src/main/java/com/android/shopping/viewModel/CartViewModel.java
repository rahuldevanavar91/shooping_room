package com.android.shopping.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.model.CartAndQty;
import com.android.shopping.network.Resource;
import com.android.shopping.repository.CartRepository;
import com.android.shopping.ui.adapter.CartListAdapter;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class CartViewModel extends AndroidViewModel {
    private MutableLiveData<Resource<List<CartAndQty>>> mMutableLiveData;
    private CartRepository cartRepository;
    private CompositeDisposable mCompositeDisposable;

    public CartViewModel(@NonNull Application application) {
        super(application);
        mMutableLiveData = new MutableLiveData<>();
        cartRepository = new CartRepository(getApplication().getApplicationContext());
        mCompositeDisposable = new CompositeDisposable();
        getCartDetails();
    }

    public MutableLiveData<Resource<List<CartAndQty>>> getMutableLiveData() {
        return mMutableLiveData;
    }

    private void getCartDetails() {
        mCompositeDisposable.add(cartRepository.getCartDetails(CartListAdapter.VIEW_TYPE_CART_LIST).subscribeWith(new DisposableSubscriber<List<CartAndQty>>() {
            @Override
            public void onNext(List<CartAndQty> details) {
                mMutableLiveData.setValue(Resource.success(details));

            }

            @Override
            public void onError(Throwable t) {
                mMutableLiveData.setValue(Resource.error(t.getMessage()));

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public void updateQty(int id, int qty) {
        cartRepository.updateQty(id, qty);
    }

    public void removeFromCart(int cartId) {
        cartRepository.removeFromCart(cartId);
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

}