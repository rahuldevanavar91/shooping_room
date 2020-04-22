package com.android.shopping.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImageViewPagerAdapter extends RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder> {
    private final Context mContext;
    private List<String> mImageList;

    public ImageViewPagerAdapter(Context context, List<String> list) {
        mImageList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_pager_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .load(mImageList.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
