package com.android.shopping.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.shopping.R;
import com.android.shopping.network.Resource;
import com.android.shopping.viewModel.LogInViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    private static final String EMAIL = "email";
    private static final int RC_SIGN_IN = 300;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private LogInViewModel logInViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_log_in);
       seToolBar();
        findViewById(R.id.google_login).setOnClickListener(this::onClick);
        findViewById(R.id.fb_login).setOnClickListener(this::onClick);
        findViewById(R.id.log_in).setOnClickListener(this::onClick);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        logInViewModel.getLogInLiveData().observe(this, this::OnChange);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        fbLogInCallBack();

    }

    private void seToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.login);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void OnChange(Resource<String> stringResource) {
        switch (stringResource.getStatus()) {
            case SUCCESS:
                Toast.makeText(getApplicationContext(), stringResource.getData(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case ERROR:
                Toast.makeText(getApplicationContext(), stringResource.getMessage(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void fbLogInCallBack() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logInViewModel.loinSuccess();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, error.getMessage());
            }
        });
    }

    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL));
                break;
            case R.id.google_login:
                signIn();
                break;
            case R.id.log_in:
                logInViewModel.checkUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                break;
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            logInViewModel.loinSuccess();
        } catch (ApiException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
