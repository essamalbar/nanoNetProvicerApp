package com.crazyiter.nanonetprovicerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.PlanManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;

public class PlansActivity extends AppCompatActivity {

    private RecyclerView plansRV;
    private TextView noTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        Provider provider = Provider.getShared(this);

        noTV = findViewById(R.id.noTV);
        plansRV = findViewById(R.id.plansRV);
        plansRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        ImageView addIV = findViewById(R.id.addIV);
        addIV.setOnClickListener(v -> startActivity(new Intent(this, AddPlanActivity.class)));

        PlanManager.getPlans(provider, planModels -> {
            if (planModels == null || planModels.isEmpty()) {
                noTV.setVisibility(View.VISIBLE);
                plansRV.setVisibility(View.GONE);
            } else {
                noTV.setVisibility(View.GONE);
                plansRV.setVisibility(View.VISIBLE);

                plansRV.setAdapter(new PlanAdapter(this, planModels));
            }
        });
    }
}