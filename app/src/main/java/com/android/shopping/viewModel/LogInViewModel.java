package com.android.shopping.viewModel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.shopping.util.PreferenceManager;
import com.android.shopping.util.Resource;

public class LogInViewModel extends AndroidViewModel {

    private MutableLiveData<Resource<String>> logIn;

    public LogInViewModel(@NonNull Application application) {
        super(application);
        logIn = new MutableLiveData<>();
    }

    public MutableLiveData<Resource<String>> getLogInLiveData() {
        return logIn;
    }

    public void checkUser(String email, String password) {
        if (!isValidEmail(email)) {
            logIn.setValue(Resource.error("Email is in valid"));
        } else if (!isValidPassword(password)) {
            logIn.setValue(Resource.error("password must be 4 characters "));
        } else {
            loinSuccess();
        }
    }

    public void loinSuccess() {
        PreferenceManager.setUseLogIn(getApplication().getApplicationContext(), true);
        logIn.setValue(Resource.success("Log in success"));

    }

    private boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private boolean isValidPassword(String password) {
        return (!TextUtils.isEmpty(password) && password.length() > 4);
    }
}
