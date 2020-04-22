package com.android.shopping.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.AddressItem;
import com.android.shopping.util.ItemClickListener;

import java.util.List;

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {
    public static int VIEW_TYPE_ADD_ADDRESS = 1;
    public static int VIEW_TYPE_ADDRESS_LIST = 0;

    private final Context mContext;
    private List<AddressItem> mAddressItems;
    private int mSelectedPos;
    private ItemClickListener mListener;

    public AddressListAdapter(Context context, List<AddressItem> list, ItemClickListener listener) {
        mAddressItems = list;
        mListener = listener;
        mContext = context;
        mSelectedPos = 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADDRESS_LIST) {
            return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.address_list_item, parent, false), viewType);
        } else {
            return new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.accent_button_layout, parent, false), viewType);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AddressItem addressItem = mAddressItems.get(position);
        if (addressItem.getViewType() == VIEW_TYPE_ADDRESS_LIST) {
            holder.mName.setText(addressItem.getName());
            holder.mAddress.setText(addressItem.getAddress());
            holder.mPhoneNumber.setText(addressItem.getPhone());
            holder.mPinCode.setText(addressItem.getPinCode());
            if (mSelectedPos == position) {
                holder.selector.setChecked(true);
                mListener.onItemClickListener(holder.itemView, position, addressItem);
            } else {
                holder.selector.setChecked(false);
            }
            holder.itemView.setTag(position);
        } else {
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mAddressItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mAddressItems.get(position).getViewType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mAddress;
        private TextView mPinCode;
        private TextView mPhoneNumber;
        private RadioButton selector;

        private Button addAddressButton;

        public ViewHolder(@NonNull View view, int viewType) {
            super(view);
            if (VIEW_TYPE_ADDRESS_LIST == viewType) {
                mName = view.findViewById(R.id.name);
                mAddress = view.findViewById(R.id.address);
                mPinCode = view.findViewById(R.id.pin_code);
                mPhoneNumber = view.findViewById(R.id.phone_number);
                selector = view.findViewById(R.id.selector);
                view.setOnClickListener(v -> {
                    mSelectedPos = (int) v.getTag();
                    notifyDataSetChanged();
                    mListener.onItemClickListener(v, mSelectedPos, mAddressItems.get(mSelectedPos));
                });
            } else {
                addAddressButton = view.findViewById(R.id.button);
                addAddressButton.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                addAddressButton.setText(R.string.add_new_address);
                addAddressButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) addAddressButton.getLayoutParams();
                layoutParams.setMargins(0, 10, 0, 30);
                addAddressButton.setOnClickListener(v -> {
                    mListener.onItemClickListener(v, (int) addAddressButton.getTag(), null);
                });
            }
        }
    }
}
