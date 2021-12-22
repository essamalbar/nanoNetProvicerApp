package com.crazyiter.nanonetprovicerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.toolbox.Volley;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.ProviderManager;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameTIL, passwordTIL;
    private CardView rootCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Provider.getShared(this) != null) {
            startApp();
        }

        rootCV = findViewById(R.id.rootCV);
        usernameTIL = findViewById(R.id.usernameTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        Button loginBTN = findViewById(R.id.loginBTN);

        loginBTN.setOnClickListener(v -> {
            String username = usernameTIL.getEditText().getText().toString().trim();
            String password = passwordTIL.getEditText().getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_username_password_error));
                return;
            }

            LoadingDialog loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
            ProviderManager.login(username, password, provider -> {
                loadingDialog.dismiss();
                if (provider == null) {
                    Statics.showSnackBar(rootCV, getString(R.string.try_again));
                } else {
                    provider.setFcm(FirebaseCloudMessagingService.getToken(this));
                    provider.setOnline(true);
                    provider.setActive(true);
                    ProviderManager.update(provider);
                    provider.saveShared(this);
                    startApp();
                }
            });
        });

    }

    private void startApp() {
        SettingsActivity.setupDate(this);
        VolleyManager.REQUEST_QUEUE = Volley.newRequestQueue(getApplicationContext());

        startActivity(new Intent(this, MainProviderActivity.class));
        finish();
    }
}