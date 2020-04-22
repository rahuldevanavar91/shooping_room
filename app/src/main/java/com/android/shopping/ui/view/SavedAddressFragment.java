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
import com.android.shopping.model.AddressItem;
import com.android.shopping.ui.adapter.AddressListAdapter;
import com.android.shopping.util.Resource;
import com.android.shopping.viewModel.AddressViewModel;

import java.util.List;

public class SavedAddressFragment extends BaseFragment {

    private AddressViewModel mAddressViewModel;
    private RecyclerView mAddressList;
    private Button mProceedButton;
    private View mAdressEmptyLayout;
    private AddressItem mSelectedAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getWidget(view);
        mProceedButton.setOnClickListener(this::onClick);
        mAddressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        mAddressViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), this::onChange);
        mAddressViewModel.getAddress();
        mAddressViewModel.getAddressLiveData();
        setTitle(getString(R.string.address), false);
    }

    private void getWidget(View view) {
        mProceedButton = view.findViewById(R.id.button);
        mProceedButton.setText(getText(R.string.proceed_to_pay));
        mAdressEmptyLayout = view.findViewById(R.id.cart_empty_layout);
        mAddressList = view.findViewById(R.id.product_list_recycler);
        ((TextView) view.findViewById(R.id.message)).setText(R.string.no_saved_address_found);
        (view.findViewById(R.id.image)).setVisibility(View.GONE);

    }

    private void onChange(Resource<List<AddressItem>> listResource) {
        switch (listResource.getStatus()) {
            case SUCCESS:
                if (listResource.getData() != null) {
                    mAdressEmptyLayout.setVisibility(View.GONE);
                    mProceedButton.setText(R.string.proceed_to_pay);
                    AddressListAdapter addressListAdapter = new AddressListAdapter(getContext(), listResource.getData(), this::onClick);
                    mAddressList.setLayoutManager(new LinearLayoutManager(getContext()));
                    mAddressList.setAdapter(addressListAdapter);
                } else {
                    mAdressEmptyLayout.setVisibility(View.VISIBLE);
                    mProceedButton.setText(R.string.add_new_address);
                }
        }
    }

    private void onClick(View view, int i, Object o) {
        if (view.getId() == R.id.button) {
            Navigation.findNavController(view).navigate(R.id.action_savedAddressFragment_to_addNewAddressFragment);
        } else {
            mSelectedAddress = (AddressItem) o;
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (((Button) view).getText().toString().equalsIgnoreCase(getString(R.string.proceed_to_pay))) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(getString(R.string.address_id), mSelectedAddress.getId());
                    Navigation.findNavController(view)
                            .navigate(R.id.action_savedAddressFragment_to_paymentFragment, bundle);
                } else {
                    Navigation.findNavController(view).navigate(R.id.action_savedAddressFragment_to_addNewAddressFragment);
                }
                break;
        }
    }
}
