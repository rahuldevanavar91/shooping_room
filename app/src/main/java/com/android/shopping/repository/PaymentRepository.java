package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.ui.adapter.CartListAdapter;

import java.util.Calendar;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class PaymentRepository {
    private CartRepository mCartRepository;
    private ProductResponseDao mDao;
    private OrderDetails mOrderDetails;

    public PaymentRepository(Context context) {
        mCartRepository = new CartRepository(context);
        mDao = AppDatabase.getInstance(context).responseDao();
    }

    public Flowable<PriceDetails> getPaymentDetails() {
        return mCartRepository.getCartDetails(CartListAdapter.VIEW_TYPE_ORDER_ITEM).map(cartDetails -> {
            PriceDetails priceDetails = null;
            if (cartDetails != null && !cartDetails.isEmpty()) {
                CartAndQty cart = cartDetails.get(0);
                priceDetails = cartDetails.get(cartDetails.size() - 1).getPriceDetails();
                cartDetails.remove(cartDetails.size() - 1);
                mOrderDetails = new OrderDetails(cartDetails,
                        String.valueOf(Calendar.getInstance().getTime()),
                        cart.getPriceDetails());

            }
            return priceDetails;

        }).subscribeOn(Schedulers.io());
    }

    public Observable<Object> createOrder() {
        return Observable.fromCallable(() -> mDao.createOrder(mOrderDetails))
                .map(result -> {
                    Completable.fromCallable(() -> mDao.clearCart())
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                    return result;
                });
    }
}
