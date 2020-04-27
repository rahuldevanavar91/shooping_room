package com.android.shopping.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.android.shopping.util.ItemClickListener;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_PRODUCT_LIST = 0;
    public static final int VIEW_TYPE_ORDER_PLACED = 1;
    private ItemClickListener mClickListener;
    private Context mContext;
    private List<ProductItem> mProductItems;

    public ProductListAdapter(Context context, List<ProductItem> data, ItemClickListener listener) {
        mClickListener = listener;
        mContext = context;
        mProductItems = data;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PRODUCT_LIST) {
            return new ProductListViewViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.product_list_item, parent, false));
        } else {
            return new OrderListViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.order_list_item, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ProductItem item = mProductItems.get(position);
        if (item.getViewType() == VIEW_TYPE_PRODUCT_LIST) {
            ((ProductListViewViewHolder) holder).setPriceData(mContext, item, position);
            holder.itemView.setTransitionName("image");
        } else {
            OrderListViewHolder orderListViewHolder = (OrderListViewHolder) holder;
            orderListViewHolder.orderId.setText(String.valueOf(item.getOrderItem().getId()));
            orderListViewHolder.orderPlacedDate.setText(item.getOrderItem().getDate());
            orderListViewHolder.cancelOrder.setTag(position);
            if (orderListViewHolder.orderListAdapter != null) {
                orderListViewHolder.orderListAdapter.update(item.getOrderItem().getProductList());
            } else {
                orderListViewHolder.orderListAdapter = new CartListAdapter(mContext, item.getOrderItem().getProductList(), mClickListener);
                orderListViewHolder.mOrderRecycler.setAdapter(orderListViewHolder.orderListAdapter);
            }

        }
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public void itemRemoved(List<ProductItem> productItems, int position) {
        mProductItems = productItems;
        notifyItemRangeChanged(position, productItems.size());
    }

    public void update(List<ProductItem> productItems) {
        mProductItems = productItems;
        notifyDataSetChanged();
    }

    public class ProductListViewViewHolder extends PriceViewHolder implements View.OnClickListener {

        public ProductListViewViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            float textSize = mContext.getResources().getDimension(R.dimen._12dp);
            price.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            offerLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            offerPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);


        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClickListener(v, (int) v.getTag(), mProductItems.get((int) v.getTag()).getProductId());

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mProductItems.get(position).getViewType();
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView mOrderRecycler;
        private CartListAdapter orderListAdapter;
        private TextView orderPlacedDate;
        private TextView orderId;
        private Button cancelOrder;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            mOrderRecycler = itemView.findViewById(R.id.recycler_view);
            mOrderRecycler.setLayoutManager(new LinearLayoutManager(mContext));
            orderPlacedDate = itemView.findViewById(R.id.date);
            orderId = itemView.findViewById(R.id.order_id);
            cancelOrder = itemView.findViewById(R.id.button);
            cancelOrder.setText(R.string.cancel_order);
            cancelOrder.setOnClickListener(v -> {
                int position = (int) v.getTag();
                int id = mProductItems.get(position).getOrderItem().getId();
                mClickListener.onItemClickListener(v, position, id);
            });

        }
    }
}
