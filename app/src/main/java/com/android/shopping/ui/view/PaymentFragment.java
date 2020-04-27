package com.android.shopping.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.shopping.R;
import com.android.shopping.model.PriceDetails;
import com.android.shopping.network.Resource;
import com.android.shopping.network.Status;
import com.android.shopping.viewModel.PaymentViewModel;

public class PaymentFragment extends BaseFragment {
    private TextView totalMrp;
    private TextView totalOfferPrice;
    private TextView totalPayable;
    private TextView shippingCharges;

    private PaymentViewModel mPaymentViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View itemView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(itemView, savedInstanceState);
        Button placeOrder = itemView.findViewById(R.id.button);
        placeOrder.setText(R.string.pay_on_delivery);
        totalMrp = itemView.findViewById(R.id.total_mrp);
        totalOfferPrice = itemView.findViewById(R.id.total_offer_price);
        shippingCharges = itemView.findViewById(R.id.shipping_charges);
        totalPayable = itemView.findViewById(R.id.total);

        placeOrder.setOnClickListener(v -> {
            mPaymentViewModel.createOrder();
            NavController controller = Navigation.findNavController(v);
            controller.popBackStack(R.id.action_savedAddressFragment_to_paymentFragment, true);
            controller.navigate(R.id.action_paymentFragment_to_orderPlace);
        });

        mPaymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        mPaymentViewModel.getResourceMutableLiveData().observe(getViewLifecycleOwner(), this::onChange);
        setTitle(getString(R.string.Payment), false);
    }

    private void onChange(Resource<PriceDetails> priceDetailsResource) {
        if (priceDetailsResource.getStatus() == Status.SUCCESS) {
            String rupeesSymbol = getString(R.string.rupee);
            totalPayable.setText(rupeesSymbol + priceDetailsResource.getData().getTotalPayable());
            shippingCharges.setText(rupeesSymbol + priceDetailsResource.getData().getShippingCharges());
            totalOfferPrice.setText(rupeesSymbol + priceDetailsResource.getData().getTotalDiscountPrice());
            totalMrp.setText(rupeesSymbol + priceDetailsResource.getData().getTotalPrice());
        } else if (priceDetailsResource.getStatus() == Status.ERROR) {
            Toast.makeText(getContext(), priceDetailsResource.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
