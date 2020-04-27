package com.android.shopping.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.ProductItem;
import com.android.shopping.network.Resource;
import com.android.shopping.ui.adapter.ProductListAdapter;
import com.android.shopping.viewModel.OrderListingViewModel;

import java.util.List;

public class OrderListingFragment extends BaseFragment {
    private RecyclerView mOrderRecycler;
    private TextView mErrorText;
    private View mEmptyLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrderRecycler = view.findViewById(R.id.product_list_recycler);
        mErrorText = view.findViewById(R.id.message);
        Button button = view.findViewById(R.id.button);
        button.setVisibility(View.GONE);
        mEmptyLayout = view.findViewById(R.id.cart_empty_layout);
        view.findViewById(R.id.image);
        OrderListingViewModel viewModel = new ViewModelProvider(this).get(OrderListingViewModel.class);
        viewModel.getResult().observe(getViewLifecycleOwner(), this::onChange);
        setTitle(getString(R.string.my_orders), false);
    }

    private void onChange(Resource<List<ProductItem>> listResource) {
        switch (listResource.getStatus()) {
            case ERROR:
                setMessage(listResource.getMessage());
                break;
            case SUCCESS:
                if (listResource.getData() != null && !listResource.getData().isEmpty()) {
                    mOrderRecycler.setVisibility(View.VISIBLE);
                    mEmptyLayout.setVisibility(View.GONE);
                    ProductListAdapter adapter = new ProductListAdapter(getContext(), listResource.getData(), this::onClick);
                    mOrderRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                    mOrderRecycler.setAdapter(adapter);
                } else {
                    setMessage(getString(R.string.no_order_found));
                }
                break;

        }
    }

    private void onClick(View view, int i, Object data) {
        Bundle args = new Bundle();
        args.putString(getString(R.string.product_id), (String) data);
        Navigation.
                findNavController(view).
                navigate(R.id.productDetailFragment
                        , args);

    }

    private void setMessage(String message) {
        mOrderRecycler.setVisibility(View.GONE);
        mErrorText.setText(message);
        mEmptyLayout.setVisibility(View.VISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }
}
