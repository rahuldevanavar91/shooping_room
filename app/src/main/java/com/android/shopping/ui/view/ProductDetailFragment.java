package com.android.shopping.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.transition.TransitionInflater;
import androidx.viewpager2.widget.ViewPager2;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.android.shopping.ui.adapter.ImageViewPagerAdapter;
import com.android.shopping.util.Resource;
import com.android.shopping.viewModel.ProductDetailViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductDetailFragment extends BaseFragment {


    private ViewPager2 mImagePager;
    private TabLayout mTabLayout;
    private Button mAddToCartButton;
    private ProgressBar mProgressBar;
    private String mProductId;
    private ProductDetailViewModel mProductDetailViewModel;
    private TextView mName;
    private TextView mOfferPrice;
    private TextView mPrice;
    private TextView mOfferLabel;
    private TextView mAboutBrand;
    private TextView mDesc;

    public ProductDetailFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWidgets(view);
        setTitle(null, true);
        setCollapsingToolBar();
        mProductId = getArguments().getString(getString(R.string.product_id));
        mProductDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        mProductDetailViewModel.getProductDetail(mProductId);
        mProductDetailViewModel.getResultLiveData().observe(getViewLifecycleOwner(), this::onChange);


    }

    private void setCollapsingToolBar() {
        Toolbar toolbar = getToolbar();
        AppBarLayout appBarLayout = getView().findViewById(R.id.app_bar_layout);
        CollapsingToolbarLayout collapsingToolbar = getView().findViewById(R.id.collapsing_tool_bar);
        RelativeLayout cartLayout = (RelativeLayout) toolbar.getMenu().getItem(0).getActionView();
        ImageView cartIcon = cartLayout.findViewById(R.id.shop_cart_icon);
        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            if (collapsingToolbar.getHeight() + verticalOffset < (2 * ViewCompat.getMinimumHeight(collapsingToolbar))) {
                if (toolbar.getNavigationIcon() != null) {
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
                }
                cartIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

            } else {
                if (toolbar.getNavigationIcon() != null) {
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                }
                cartIcon.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        });
        if (toolbar.getMenu().size() > 1) {
            toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.getMenu().getItem(2).setVisible(false);
        }

    }

    private void getWidgets(View itemView) {
        mName = itemView.findViewById(R.id.product_name);
        mPrice = itemView.findViewById(R.id.price);
        mOfferLabel = itemView.findViewById(R.id.offer_label);
        mOfferPrice = itemView.findViewById(R.id.offer_price);
        mPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mAboutBrand = itemView.findViewById(R.id.about_brand);
        mDesc = itemView.findViewById(R.id.product_desc);
        mImagePager = itemView.findViewById(R.id.view_pager);
        mTabLayout = itemView.findViewById(R.id.tab_layout);
        mAddToCartButton = itemView.findViewById(R.id.button);
        mProgressBar = itemView.findViewById(R.id.progress_bar);
        mAddToCartButton.setText(getText(R.string.add_to_cart));
        mAddToCartButton.setOnClickListener(v -> {
            if (((Button) v).getText().toString().equalsIgnoreCase(getString(R.string.add_to_cart))) {

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(mAddToCartButton, "scaleX", 1f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(mAddToCartButton, "scaleX", 0f, 1f);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa2.start();
                    }
                });
                oa1.start();
               mProductDetailViewModel.requestForAddToCart(mProductId);
            } else {
                Navigation.findNavController(v)
                        .navigate(R.id.action_productDetailFragment_to_cartFragment);
            }
        });

    }

    private void onChange(Resource<ProductItem> productItemResource) {
        mProgressBar.setVisibility(View.GONE);
        switch (productItemResource.getStatus()) {
            case SUCCESS:
                switch (productItemResource.getRequestType()) {
                    case getProductDetail:
                        ImageViewPagerAdapter adapter = new ImageViewPagerAdapter(getContext(), productItemResource.getData().getImage());
                        mImagePager.setAdapter(adapter);
                        mImagePager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
                        new TabLayoutMediator(mTabLayout, mImagePager, (tab, position) -> {
                        }).attach();
                        setData(productItemResource.getData());
                        mProductDetailViewModel.checkInCart(mProductId);
                        break;
                    case addToCart:
                        mAddToCartButton.setText(R.string.item_in_cart);
                        break;
                    case checkInCart:
                        if (productItemResource.getData().getIsInCart() == 1) {
                            mAddToCartButton.setText(R.string.item_in_cart);
                        } else {
                            mAddToCartButton.setText(R.string.add_to_cart);
                        }
                        break;
                }
                break;
            case LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                Toast.makeText(getContext(), productItemResource.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setData(ProductItem item) {
        String rupeesLabel = getString(R.string.rupee);
        mName.setText(item.getName());
        if (item.getPrice() == item.getOfferPrice()) {
            mPrice.setVisibility(View.GONE);
            mOfferPrice.setVisibility(View.VISIBLE);
            mOfferLabel.setVisibility(View.GONE);
            mOfferPrice.setText(rupeesLabel + item.getPrice());
        } else {
            mPrice.setVisibility(View.VISIBLE);
            mOfferLabel.setVisibility(View.VISIBLE);
            mOfferPrice.setVisibility(View.VISIBLE);
            mOfferPrice.setText(rupeesLabel + item.getOfferPrice());
            mPrice.setText(rupeesLabel + item.getPrice());
            mOfferLabel.setText(item.getOfferLabel() + "%OFF");
        }
        mAboutBrand.setText(item.getAboutBrand());
        mDesc.setText(item.getDesc());

    }
}
