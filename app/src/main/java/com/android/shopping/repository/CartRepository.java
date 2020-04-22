package com.android.shopping.repository;

import android.content.Context;
import android.util.Log;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.ui.adapter.CartListAdapter;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CartRepository {
    private ProductResponseDao mDao;

    public CartRepository(Context context) {
        mDao = AppDatabase.getInstance(context).responseDao();

    }

    public Flowable<List<CartAndQty>> getCartDetails() {
        return mDao.getCartDetails()
                .subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .map(details -> {
                    CartAndQty cartResponse = new CartAndQty();

                    if (details != null && !details.isEmpty()) {
                        int totalMrp = 0, totalOfferPrice = 0;
                        for (CartAndQty item : details) {
                            item.setViewType(CartListAdapter.VIEW_TYPE_LIST);
                            item.getProductItem().setPrice(item.getProductItem().getPrice() * item.getQty());
                            item.getProductItem().setOfferPrice(item.getProductItem().getOfferPrice() * item.getQty());
                            totalMrp += item.getProductItem().getPrice();
                            item.getProductItem().setQty(item.getQty());
                            totalOfferPrice += item.getProductItem().getOfferPrice();
                        }
                        PriceDetails priceDetails = new PriceDetails(totalMrp, totalOfferPrice, 50, totalOfferPrice + 50);
                        cartResponse.setViewType(CartListAdapter.VIEW_TYPE_FOOTER);
                        cartResponse.setPriceDetails(priceDetails);
                        details.add(cartResponse);
                        return details;
                    }
                    return new ArrayList<>();
                });
    }

    public void removeFromCart(int cartId) {
        Observable.fromCallable(() -> mDao.removeFromCart(cartId)).subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("Rahul", "removed " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Rahul", "removed errror " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateQty(int id, int qty) {
        Completable.fromAction(() -> mDao.updateQty(id, qty)).subscribeOn(Schedulers.io())
                .subscribe();
    }
}
