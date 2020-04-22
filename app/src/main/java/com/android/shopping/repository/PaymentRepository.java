package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.model.ProductItem;
import com.android.shopping.ui.adapter.ProductListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        return mCartRepository.getCartDetails().map(cartDetails -> {
            PriceDetails priceDetails = null;
            if (cartDetails != null && !cartDetails.isEmpty()) {
                CartAndQty cart = cartDetails.get(0);
                List<ProductItem> productItems = new ArrayList<>();
                for (int i = 0; i < cartDetails.size() - 1; i++) {
                    CartAndQty cartAndQty = cartDetails.get(i);
                    ProductItem productItem = cartAndQty.getProductItem();
                    productItem.setViewType(ProductListAdapter.VIEW_TYPE_ORDER_PLACED);
                    productItems.add(productItem);
                }
                priceDetails = cartDetails.get(cartDetails.size() - 1).getPriceDetails();
                mOrderDetails = new OrderDetails(productItems,
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
