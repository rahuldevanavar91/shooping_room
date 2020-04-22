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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.shopping.R;
import com.android.shopping.model.AddressItem;
import com.android.shopping.util.Resource;
import com.android.shopping.viewModel.AddressViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AddNewAddressFragment extends BaseFragment {


    private Button mSaveAddress;
    private TextInputEditText mName;
    private TextInputEditText mAddress;
    private TextInputEditText mPinCode;
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mLandmark;
    private TextInputEditText mCity;
    private TextInputEditText mSate;
    private AddressViewModel mAddressViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_address, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSaveAddress = view.findViewById(R.id.button);
        mSaveAddress.setText(getText(R.string.save));
        mName = view.findViewById(R.id.name);
        mAddress = view.findViewById(R.id.house_number);
        mPinCode = view.findViewById(R.id.pin_code);
        mPhoneNumber = view.findViewById(R.id.phone_number);
        mLandmark = view.findViewById(R.id.land_mark);
        mCity = view.findViewById(R.id.city);
        mSate = view.findViewById(R.id.state);
        mSaveAddress.setOnClickListener(this::onClick);
        mAddressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        mAddressViewModel.getAddressLiveData().observe(getViewLifecycleOwner(), this::onAddressSave);
        setTitle(getString(R.string.add_address), false);

    }

    private void onAddressSave(Resource<List<AddressItem>> listResource) {
        switch (listResource.getStatus()) {
            case SUCCESS:
                NavController navController = Navigation.findNavController(getView());
                navController.popBackStack();
                break;
            case ERROR:
                Toast.makeText(getContext(), listResource.getMessage(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (isAllFiledValid()) {
                    AddressItem addressItem = new AddressItem();
                    addressItem.setName(mName.getText().toString());
                    addressItem.setAddress(mAddress.getText().toString());
                    addressItem.setCity(mCity.getText().toString());
                    addressItem.setSate(mSate.getText().toString());
                    addressItem.setPhone(mPhoneNumber.getText().toString());
                    addressItem.setPinCode(mPinCode.getText().toString());
                    addressItem.setLandmark(mLandmark.getText().toString());
                    mAddressViewModel.saveAddress(addressItem);
                }
        }
    }

    private boolean isAllFiledValid() {
        if (mName.getText().toString().trim().isEmpty()) {
            mName.setError(getString(R.string.name_empty_error_message));
            mName.setFocusable(true);
            return false;
        } else if (mAddress.getText().toString().trim().isEmpty()) {
            mAddress.setError(getString(R.string.address_error));
            mAddress.setFocusable(true);
            return false;
        } else if (mPinCode.getText().toString().trim().isEmpty() ||
                mPinCode.getText().toString().length() < 6) {
            mPinCode.setError(getString(R.string.pinco_error));
            mPinCode.setFocusable(true);
            return false;
        } else if (mCity.getText().toString().trim().isEmpty()) {
            mCity.setError(getString(R.string.city_error));
            mCity.setFocusable(true);
            return false;
        } else if (mSate.getText().toString().trim().isEmpty()) {
            mSate.setError(getString(R.string.state_error));
            mSate.setFocusable(true);
            return false;
        } else if (mPhoneNumber.getText().toString().length() < 10) {
            mPhoneNumber.setFocusable(true);
            mPhoneNumber.setError(getString(R.string.phone_error));
            return false;
        } else {
            return true;
        }
    }
}
