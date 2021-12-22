package com.crazyiter.nanonetprovicerapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.crazyiter.nanonetprovicerapp.db.PlanManager;
import com.crazyiter.nanonetprovicerapp.db.PlanModel;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.textfield.TextInputLayout;

public class AddPlanActivity extends AppCompatActivity {

    private Provider provider;
    private CardView rootCV;
    private TextInputLayout nameTIL;
    private TextInputLayout priceTIL;
    private TextInputLayout descriptionTIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        provider = Provider.getShared(this);

        rootCV = findViewById(R.id.rootCV);
        nameTIL = findViewById(R.id.nameTIL);
        priceTIL = findViewById(R.id.costTIL);
        descriptionTIL = findViewById(R.id.descriptionTIL);

        Button addBTN = findViewById(R.id.addBTN);
        addBTN.setOnClickListener(v -> {
            String name = nameTIL.getEditText().getText().toString().trim();
            String price = priceTIL.getEditText().getText().toString().trim();
            String description = descriptionTIL.getEditText().getText().toString().trim();

            if (name.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_name));
                return;
            }

            if (price.isEmpty()) {
                Statics.showSnackBar(rootCV, getString(R.string.enter_cost));
                return;
            }

            PlanManager.addPlan(new PlanModel("", name, Integer.parseInt(price), description, provider.getId()));
            finish();
        });
    }

}