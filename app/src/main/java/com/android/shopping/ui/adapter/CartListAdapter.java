package com.android.shopping.ui.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.util.ItemClickListener;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_FOOTER = 2;
    public static final int VIEW_TYPE_CART_LIST = 1;
    public static final int VIEW_TYPE_ORDER_ITEM = 3;

    private final Context mContext;
    private final ItemClickListener mListner;
    private final ArrayAdapter<Integer> spinnerAdapter;
    private List<CartAndQty> mList;
    private int mHorizontalOffset;

    public CartListAdapter(Context context, List<CartAndQty> list, ItemClickListener listener) {
        mContext = context;
        mListner = listener;
        mList = list;
        mHorizontalOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
        spinnerAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_layout);
        for (int i = 1; i <= 5; i++) {
            spinnerAdapter.add(i);
        }
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CART_LIST || viewType == VIEW_TYPE_ORDER_ITEM) {
            return new ListItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cart_list_item, parent, false), viewType);
        } else {
            return new FooterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.price_details_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        CartAndQty item = mList.get(position);
        if (item.getViewType() == VIEW_TYPE_CART_LIST || item.getViewType() == VIEW_TYPE_ORDER_ITEM) {
            ListItemViewHolder listItemViewHolder = (ListItemViewHolder) holder;
            listItemViewHolder.setPriceData(mContext, item.getProductItem(), position);
            if (item.getViewType() == VIEW_TYPE_CART_LIST) {
                listItemViewHolder.removeFromCart.setTag(position);
                setQuantitySpinner(position, (ListItemViewHolder) holder, item.getQty(), 5);
            } else {
                ((ListItemViewHolder) holder).qty.setText("Qty : " + item.getQty());
            }
        } else {
            setFooter(item.getPriceDetails(), (FooterViewHolder) holder);
        }
    }

    private void setFooter(PriceDetails item, FooterViewHolder holder) {
        String rupeesSymbol = mContext.getString(R.string.rupee);
        holder.totalPayable.setText(rupeesSymbol + item.getTotalPayable());
        holder.shippingCharges.setText(rupeesSymbol + item.getShippingCharges());
        holder.totalOfferPrice.setText(rupeesSymbol + item.getTotalDiscountPrice());
        holder.totalMrp.setText(rupeesSymbol + item.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void setQuantitySpinner(int position, ListItemViewHolder holder, int selectedQuantity, int maxOrderQuantity) {
        holder.qtySpinner.setTag(position);

        holder.qtySpinner.setAdapter(spinnerAdapter);
        holder.qtySpinner.setDropDownHorizontalOffset(mHorizontalOffset);
        holder.qtySpinner.setSelection(selectedQuantity - 1);

        holder.qtySpinner.setTag(R.integer.selected_pos, selectedQuantity - 1);
        holder.qtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getTag() != null &&
                        ((int) parent.getTag(R.integer.selected_pos)) == position) {
                    return;
                }
                mList.get(holder.getAdapterPosition()).setQty((Integer) parent.getItemAtPosition(position));
                notifyItemChanged(holder.getAdapterPosition());
                mListner.onItemClickListener(parent, holder.getAdapterPosition(), mList.get(holder.getAdapterPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void update(List<CartAndQty> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    public class ListItemViewHolder extends PriceViewHolder implements View.OnClickListener {
        private Spinner qtySpinner;
        private View removeFromCart;
        private TextView qty;

        public ListItemViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            productImage.setOnClickListener(this);
            qtySpinner = itemView.findViewById(R.id.qty_spinner);
            removeFromCart = itemView.findViewById(R.id.remove);
            qty = itemView.findViewById(R.id.qty);
            removeFromCart.setOnClickListener(this);
            qtySpinner.setClickable(true);
            qtySpinner.setEnabled(true);
            qtySpinner.setVisibility(View.VISIBLE);
            removeFromCart.setVisibility(View.VISIBLE);
            productName.setMaxLines(3);
            if (viewType == VIEW_TYPE_ORDER_ITEM) {
                removeFromCart.setVisibility(View.GONE);
                qtySpinner.setVisibility(View.GONE);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
            }
        }

        @Override
        public void onClick(View v) {
            int pos = (int) v.getTag();
            if (v.getId() == R.id.remove) {
                mListner.onItemClickListener(v, pos, mList.get(pos));
            } else {
                mListner.onItemClickListener(v, pos, mList.get(pos).getProductItem().getProductId());
            }

        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        private TextView totalMrp;
        private TextView totalOfferPrice;
        private TextView totalPayable;
        private TextView shippingCharges;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            totalMrp = itemView.findViewById(R.id.total_mrp);
            totalOfferPrice = itemView.findViewById(R.id.total_offer_price);
            shippingCharges = itemView.findViewById(R.id.shipping_charges);
            totalPayable = itemView.findViewById(R.id.total);

        }
    }
}
