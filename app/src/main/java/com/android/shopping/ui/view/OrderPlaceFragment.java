package com.android.shopping.ui.view;

import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.android.shopping.R;

public class OrderPlaceFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fargment_order_placed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(getString(R.string.order_confirm), false);
        Button button = view.findViewById(R.id.button);
        ImageView image = view.findViewById(R.id.tick_image);
        ((Animatable) image.getDrawable()).start();

        button.setText(R.string.contine_shopping);
        button.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
