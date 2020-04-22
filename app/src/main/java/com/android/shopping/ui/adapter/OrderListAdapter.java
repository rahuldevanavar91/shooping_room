package com.android.shopping.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.OrderDetails;
import com.android.shopping.util.ItemClickListener;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<OrderDetails> mOrderList;
    private Context mContext;
    private ItemClickListener mListener;

    public OrderListAdapter(Context context, List<OrderDetails> list, ItemClickListener listener) {
        mOrderList = list;
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.order_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetails item = mOrderList.get(position);
        if (holder.cartListAdapter == null) {
            holder.cartListAdapter = new ProductListAdapter(mContext, item.getProductItem(), mListener);
            holder.recyclerView.setAdapter(holder.cartListAdapter);
        } else {
            holder.cartListAdapter.update(item.getProductItem());
        }
        holder.orderId.setText(String.valueOf(item.getId()));
        holder.orderPlacedDate.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private TextView orderPlacedDate;
        private TextView orderId;
        private ProductListAdapter cartListAdapter;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderPlacedDate = itemView.findViewById(R.id.date);
            orderId = itemView.findViewById(R.id.order_id);
            recyclerView = itemView.findViewById(R.id.product_list_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
    }
}
