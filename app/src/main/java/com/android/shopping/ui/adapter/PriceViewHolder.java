package com.android.shopping.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.bumptech.glide.Glide;

public class PriceViewHolder extends RecyclerView.ViewHolder {
    protected ImageView productImage;
    protected TextView productName;
    protected TextView price;
    protected TextView offerLabel;
    protected TextView offerPrice;

    public PriceViewHolder(@NonNull View itemView) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name);
        price = itemView.findViewById(R.id.price);
        offerLabel = itemView.findViewById(R.id.offer_label);
        offerPrice = itemView.findViewById(R.id.offer_price);
        price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

    }

    public void setPriceData(Context context, ProductItem item, int position) {
        String rupeesLabel = context.getString(R.string.rupee);
        Glide.with(context)
                .load(item.getThumbImage())
                .into(productImage);
        productImage.setTag(position);
        productName.setText(item.getName());
        if (item.getPrice() == item.getOfferPrice()) {
            price.setVisibility(View.GONE);
            offerPrice.setVisibility(View.VISIBLE);
            offerLabel.setVisibility(View.GONE);
            offerPrice.setText(rupeesLabel + item.getPrice());
        } else {
            price.setVisibility(View.VISIBLE);
            offerPrice.setVisibility(View.VISIBLE);
            offerLabel.setVisibility(View.VISIBLE);
            offerPrice.setText(rupeesLabel + item.getOfferPrice());
            price.setText(rupeesLabel + item.getPrice());
            offerLabel.setText(item.getOfferLabel() + "%OFF");
        }


    }
}
