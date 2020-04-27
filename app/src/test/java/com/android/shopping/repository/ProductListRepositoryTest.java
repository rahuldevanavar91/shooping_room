package com.android.shopping.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.model.ProductItem;
import com.android.shopping.model.ProductResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.TestScheduler;

public class ProductListRepositoryTest {
    private ProductListRepository productListRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        AndroidSchedulers.mainThread();
        productListRepository = new ProductListRepository();
        TestScheduler testScheduler = new TestScheduler();
        RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
    }

    @Test
    public void test_cancel_order() {
        productListRepository.addProductToList(getProductResponse());
        int size = productListRepository.getProductItems().size();
        productListRepository.removeOrderFromList(0);
        Assert.assertEquals(productListRepository.getProductItems().size(), size - 1);
    }

    @Test
    public void test_add_product() {
        productListRepository.addProductToList(getProductResponse());
        Assert.assertNotNull(productListRepository.getProductItems());
    }

    @Test
    public void test_add_order_to_product_list() {
        int size = productListRepository.getProductItems().size();
        productListRepository.addOrderToProductList(getOrderList());
        Assert.assertEquals(productListRepository.getProductItems().size(), size + 1);
        Assert.assertNotNull(productListRepository.getProductItems());
    }

    private List<OrderDetails> getOrderList() {
        List<OrderDetails> list = new ArrayList<>();
        PriceDetails priceDetails = new PriceDetails(1, 2, 3, 4);
        list.add(new OrderDetails(getProductLists(), "", priceDetails));
        return list;
    }

    private List<CartAndQty> getProductLists() {
        CartAndQty productItem = new CartAndQty();
        List<CartAndQty> productItems = new ArrayList<>();
        productItems.add(productItem);
        productItems.add(productItem);
        return productItems;
    }

    private ProductResponse getProductResponse() {
        ProductResponse productResponse = new ProductResponse();
        ProductItem productItem = new ProductItem();
        List<ProductItem> productItems = new ArrayList<>();
        productItems.add(productItem);
        productResponse.setProducts(productItems);
        return productResponse;
    }
}