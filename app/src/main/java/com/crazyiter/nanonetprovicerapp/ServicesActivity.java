package com.crazyiter.nanonetprovicerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.ServiceManager;

public class ServicesActivity extends AppCompatActivity {

    private RecyclerView servicesRV;
    private TextView noTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Provider provider = Provider.getShared(this);

        noTV = findViewById(R.id.noTV);
        servicesRV = findViewById(R.id.servicesRV);
        servicesRV.setLayoutManager(new GridLayoutManager(this, 2));

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        ImageView addIV = findViewById(R.id.addIV);
        addIV.setOnClickListener(v -> startActivity(new Intent(this, AddServiceActivity.class)));

        ServiceManager.getMyServices(provider, serviceModels -> {
            if (serviceModels == null || serviceModels.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                servicesRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                servicesRV.setVisibility(View.VISIBLE);

                servicesRV.setAdapter(new ServiceAdapter(this, serviceModels));
            }
        });
    }

}