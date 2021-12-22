package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.ProviderManager;

import java.util.HashMap;
import java.util.Map;

public class AlertsSettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_settings);

        provider = Provider.getShared(this);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        SwitchCompat addSC = findViewById(R.id.addSC);
        addSC.setOnCheckedChangeListener(this);

        SwitchCompat paySC = findViewById(R.id.paySC);
        paySC.setOnCheckedChangeListener(this);

        SwitchCompat amountSC = findViewById(R.id.amountSC);
        amountSC.setOnCheckedChangeListener(this);

        SwitchCompat ticketSC = findViewById(R.id.ticketSC);
        ticketSC.setOnCheckedChangeListener(this);

        addSC.setChecked(provider.isRenewAlert());
        paySC.setChecked(provider.isPayAlert());
        amountSC.setChecked(provider.isAmountAlert());
        ticketSC.setChecked(provider.isTicketAlert());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.addSC:
                provider.setRenewAlert(isChecked);
                break;

            case R.id.paySC:
                provider.setPayAlert(isChecked);
                break;

            case R.id.amountSC:
                provider.setAmountAlert(isChecked);
                break;

            case R.id.ticketSC:
                provider.setTicketAlert(isChecked);
                break;
        }
        provider.saveShared(this);
        ProviderManager.update(provider);
    }
}