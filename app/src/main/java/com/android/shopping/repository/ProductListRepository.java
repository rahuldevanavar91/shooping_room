package com.android.shopping.repository;

import android.content.Context;

import com.android.shopping.database.AppDatabase;
import com.android.shopping.database.ProductResponseDao;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.ProductItem;
import com.android.shopping.model.ProductResponse;
import com.android.shopping.network.ApiEndPoint;
import com.android.shopping.network.RetrofitService;
import com.android.shopping.ui.adapter.ProductListAdapter;
import com.android.shopping.util.Util;

import java.util.ArrayList;
import java.util.List;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
import io.reactivex.schedulers.Schedulers;

public class ProductListRepository {

    private ProductResponseDao mProductResponseDao;
    private ApiEndPoint mApiEndPoint;
    private List<ProductItem> productItems;
    private CompositeDisposable compositeDisposable;
    private Context mContext;

    ProductListRepository() {
        productItems = new ArrayList<>();
        mApiEndPoint = RetrofitService.getRetrofitInstance().create(ApiEndPoint.class);
        compositeDisposable = new CompositeDisposable();
    }

    public ProductListRepository(Context context) {
        this();
        mContext = context;
        mProductResponseDao = AppDatabase.getInstance(context).responseDao();
    }

    public Flowable<List<ProductItem>> getProductList() {
        if (Util.isNetworkConnected(mContext)) {
            return getDatFromAPI();
        } else {
            return getDataFromDb();
        }
    }

    public Flowable<List<ProductItem>> getDatFromAPI() {
        return mApiEndPoint.getProducts().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .map(productResponse -> {
                    getOrderList();
                    addProductToList(productResponse);
                    return productItems;
                });

    }

    public void addProductToList(ProductResponse productResponse) {
        if (productResponse != null && productResponse.getProducts() != null) {
            insertProductsToDb(productResponse.getProducts());
            productItems.addAll(0, productResponse.getProducts());
        }

    }


    private Flowable<List<ProductItem>> getDataFromDb() {
        return mProductResponseDao.getListingProducts().map(products -> {
            productItems = products;
            getOrderList();
            return productItems;
        });
    }

    private void insertProductsToDb(List<ProductItem> products) {
        Completable.fromCallable(() -> mProductResponseDao.insertAllProducts(products))
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    private void getOrderList() {
        compositeDisposable.add(mProductResponseDao.getOrderDetails().subscribeOn(Schedulers.io())
                .observeOn(RxJavaBridge.toV2Scheduler(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableSubscriber<List<OrderDetails>>() {
                    @Override
                    public void onNext(List<OrderDetails> list) {
                        if (list != null) {
                            addOrderToProductList(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    void addOrderToProductList(List<OrderDetails> list) {
        for (OrderDetails orderDetails : list) {
            ProductItem productItem = new ProductItem();
            productItem.setViewType(ProductListAdapter.VIEW_TYPE_ORDER_PLACED);
            productItem.setOrderItem(orderDetails);
            if (!productItems.contains(productItem)) {
                productItems.add(productItem);
            }
        }
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public Observable<List<ProductItem>> cancelOrder(int parentPosition, int orderId) {
        return Observable.fromCallable(() -> mProductResponseDao.cancelOrder(orderId)).subscribeOn(Schedulers.io())
                .map(integer -> {
                    removeOrderFromList(parentPosition);
                    return productItems;
                });
    }

    void removeOrderFromList(int parentPosition) {
        productItems.remove(parentPosition);

    }
}
