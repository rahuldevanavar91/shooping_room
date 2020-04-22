package com.android.shopping.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.shopping.model.AddressItem;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.model.ProductItem;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ProductResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllProducts(List<ProductItem> productItems);

    @Query("select product_id,thumb_image,name,price,offer_price,offer_label,view_type from product_list")
    Flowable<List<ProductItem>> getListingProducts();

    @Query("select * from product_list where product_id=:id")
    Single<ProductItem> getProductDetail(String id);

    @Query("insert into cart (`id` ,`qty`) values (:id,:qty)")
    long insertIntoCart(String id, int qty);

    @Query("select count(*) from cart where id=:productId")
    int isInCart(String productId);

    @Query("update cart set qty=:qty where cart_id=:id")
    int updateQty(int id, int qty);

    @Query("delete from cart where cart_id=:cartId")
    int removeFromCart(int cartId);

    @Query(("select product_list.*," +
            "cart.qty,cart.cart_id from product_list inner join cart on cart.id = product_list.product_id "))
    Flowable<List<CartAndQty>> getCartDetails();


    @Query("delete from cart")
    int clearCart();

    @Insert
    long saveAddress(AddressItem addressItem);

    @Query("select * from address")
    Single<List<AddressItem>> getAddress();

    @Query(("delete from address where id=:id"))
    int deleteAddress(int id);

    @Insert
    long createOrder(OrderDetails details);

    @Query("Select * from order_details")
    Single<List<OrderDetails>> getOrderDetails();


    @Query("select qty from cart")
    Flowable<List<Integer>> getCartCount();


    @Query("SELECT * FROM product_list WHERE product_id IN (SELECT product_id FROM product_list ORDER BY RANDOM() LIMIT 1)")
    ProductItem getRandomProduct();
}
