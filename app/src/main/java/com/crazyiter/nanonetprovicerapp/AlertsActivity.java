package com.crazyiter.nanonetprovicerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.AlertManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Collections;

public class AlertsActivity extends AppCompatActivity {

    private RecyclerView alertsRV;
    private TextView noTV;
    private Provider provider;
    private ExtendedFloatingActionButton sendFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        provider = Provider.getShared(this);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        sendFAB = findViewById(R.id.sendFAB);
        sendFAB.setOnClickListener(v -> startActivity(new Intent(this, SendAlertActivity.class)));

        noTV = findViewById(R.id.noTV);
        alertsRV = findViewById(R.id.alertsRV);
        alertsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        alertsRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && sendFAB.getVisibility() == View.VISIBLE) {
                    sendFAB.hide();
                } else if (dy < 0 && sendFAB.getVisibility() != View.VISIBLE) {
                    sendFAB.show();
                }
            }
        });

        setupData();

    }

    private void setupData() {
        AlertManager.getAlerts(provider, alerts -> {
            if (alerts == null || alerts.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                sendFAB.setVisibility(View.VISIBLE);
                alertsRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                alertsRV.setVisibility(View.VISIBLE);
                alertsRV.setAdapter(new AlertsAdapter(this, alerts));
            }
        });
    }

}