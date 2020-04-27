package com.android.shopping.util;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class NoPredicativeAnimGridLayoutManager extends GridLayoutManager {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public NoPredicativeAnimGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NoPredicativeAnimGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
}