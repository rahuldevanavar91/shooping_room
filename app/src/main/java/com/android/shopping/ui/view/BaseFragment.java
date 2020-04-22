package com.android.shopping.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.shopping.R;
import com.android.shopping.util.PreferenceManager;
import com.android.shopping.util.Resource;
import com.android.shopping.viewModel.BaseViewModel;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class BaseFragment extends Fragment {


    private Toolbar mToolbar;
    private TextView mCartCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            NavController navController = Navigation.findNavController(view);
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupWithNavController(
                    mToolbar, navController, appBarConfiguration);
        }
        BaseViewModel baseViewModel = new ViewModelProvider(getActivity()).get(BaseViewModel.class);
        baseViewModel.getCartCount();
        baseViewModel.getData().observe(getViewLifecycleOwner(), this::onChange);
    }

    private void onChange(Resource<String> stringResource) {
        switch (stringResource.getStatus()) {
            case SUCCESS:
                if (mCartCount == null) return;
                if (stringResource.getData().equalsIgnoreCase("0")) {
                    mCartCount.setVisibility(View.GONE);
                } else {
                    mCartCount.setVisibility(View.VISIBLE);
                    mCartCount.setText(stringResource.getData());

                }
        }
    }


    void setTitle(String title, boolean hasMenu) {
        if (mToolbar != null) {
            if (hasMenu) {
                mToolbar.inflateMenu(R.menu.main_menu);
                setToolBar();
            }
            if (title != null) {
                mToolbar.setTitle(title);
            }
            mToolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private void setToolBar() {
        NavController navController = Navigation.findNavController(getView());
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        MenuItem menuItem = mToolbar.getMenu().getItem(0);
        RelativeLayout cart = (RelativeLayout) menuItem.getActionView();
        mCartCount = cart.findViewById(R.id.cart_count);
        cart.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.cartFragment);
        });
        mToolbar.getMenu().getItem(1).setOnMenuItemClickListener(item -> {
            Navigation.findNavController(getView()).navigate(R.id.orderListingFragment);
            return true;
        });
        mToolbar.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logOutSession();
                Intent logIn = new Intent(getContext(), LogInActivity.class);
                startActivity(logIn);
                getActivity().finish();
                return true;
            }
        });
        NavigationUI.setupWithNavController(
                mToolbar, navController, appBarConfiguration);
    }

    private void logOutSession() {
        LoginManager.getInstance().logOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();
        PreferenceManager.setUseLogIn(getContext(), false);
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        googleSignInClient.signOut();

    }
}
