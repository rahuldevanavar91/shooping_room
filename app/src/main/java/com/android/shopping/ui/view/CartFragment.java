package com.android.shopping.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.shopping.R;
import com.android.shopping.model.CartAndQty;
import com.android.shopping.ui.adapter.CartListAdapter;
import com.android.shopping.network.Resource;
import com.android.shopping.viewModel.CartViewModel;

import java.util.List;

public class CartFragment extends BaseFragment {

    private CartViewModel mCartViewModel;
    private RecyclerView mRecyclerView;
    private View cartEmptyLayout;
    private Button mPlaceOrder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.product_list_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartEmptyLayout = view.findViewById(R.id.cart_empty_layout);
        mPlaceOrder = view.findViewById(R.id.button);
        mPlaceOrder.setText(getText(R.string.proceed_to_pay));
        mPlaceOrder.setOnClickListener(this::onClick);
        mCartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mCartViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), this::onChange);
        setTitle(getString(R.string.cart), false);
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Navigation.findNavController(view)
                        .navigate(R.id.action_cartFragment_to_savedAddressFragment);
                break;
        }
    }

    private void onChange(Resource<List<CartAndQty>> cartDetailsResource) {
        switch (cartDetailsResource.getStatus()) {
            case SUCCESS:
                if (cartDetailsResource.getData() != null && !cartDetailsResource.getData().isEmpty()) {
                    cartEmptyLayout.setVisibility(View.GONE);
                    mPlaceOrder.setVisibility(View.VISIBLE);
                    CartListAdapter mAdapter = new CartListAdapter(getContext(), cartDetailsResource.getData(), this::onItemClick);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mPlaceOrder.setVisibility(View.GONE);
                    cartEmptyLayout.setVisibility(View.VISIBLE);
                }
                break;
            case ERROR:
                Toast.makeText(getContext(), cartDetailsResource.getMessage(), Toast.LENGTH_LONG).show();
                break;

        }
    }

    private void onItemClick(View view, int i, Object data) {
        switch (view.getId()) {
            case R.id.qty_spinner:
                CartAndQty cartItem = (CartAndQty) data;
                mCartViewModel.updateQty(cartItem.getCart_id(), cartItem.getQty());
                break;
            case R.id.remove:
                cartItem = (CartAndQty) data;
                mCartViewModel.removeFromCart(cartItem.getCart_id());
                break;
            case R.id.product_image:
                cartItem = (CartAndQty) data;
                Bundle args = new Bundle();
                args.putString(getString(R.string.product_id), cartItem.getProductItem().getProductId());
                Navigation.
                        findNavController(view).
                        navigate(R.id.action_cartFragment_to_productDetailFragment
                                , args);
                break;
        }
    }
}
