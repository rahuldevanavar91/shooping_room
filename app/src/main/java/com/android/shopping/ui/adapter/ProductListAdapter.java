package com.android.shopping.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
            return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.product_list_item, parent, false));
        } else {
            return new OrderListViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.cart_list_item, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        ProductItem item = mProductItems.get(position);
        if (item.getViewType() == VIEW_TYPE_PRODUCT_LIST) {
            ((ViewHolder) holder).setPriceData(mContext, item, position);
            holder.itemView.setTransitionName("image");
        } else {
            OrderListViewHolder orderListViewHolder = ((OrderListViewHolder) holder);
            orderListViewHolder.qty.setText("Qty : " + item.getQty());
            ((OrderListViewHolder) holder).setPriceData(mContext, item, position);
        }
    }

    @Override
    public int getItemCount() {
        return mProductItems.size();
    }

    public void update(List<ProductItem> list) {
        mProductItems = list;
    }

    public class ViewHolder extends PriceViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
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

    public class OrderListViewHolder extends PriceViewHolder {
        private TextView qty;

        public OrderListViewHolder(@NonNull View itemView) {
            super(itemView);
            qty = itemView.findViewById(R.id.qty);
            itemView.findViewById(R.id.remove).setVisibility(View.GONE);
            itemView.findViewById(R.id.qty_spinner).setVisibility(View.GONE);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 30, 0, 0);
            itemView.setOnClickListener(v -> {
                mClickListener.onItemClickListener(v, (int) v.getTag(), mProductItems.get((int) v.getTag()).getProductId());
            });
        }
    }
}
