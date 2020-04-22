package com.android.shopping.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.shopping.R;
import com.android.shopping.util.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            Intent intent;

            if (PreferenceManager.isUserLoggedIn(getApplicationContext())) {
                intent = new Intent(this, MainActivity.class);
            } else {
                intent = new Intent(this, LogInActivity.class);
            }
            startActivity(intent);
        }, 200);
    }
}
