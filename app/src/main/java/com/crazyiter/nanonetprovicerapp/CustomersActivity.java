package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class CustomersActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView customersRV;
    private TextView noTV;
    private TextInputLayout searchTIL;
    private Provider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        provider = Provider.getShared(this);

        searchTIL = findViewById(R.id.searchTIL);
        customersRV = findViewById(R.id.customersRV);
        noTV = findViewById(R.id.noTV);

        ImageView searchIV = findViewById(R.id.searchIV);
        ImageView backIV = findViewById(R.id.backIV);
        searchIV.setOnClickListener(this);
        backIV.setOnClickListener(this);

        FloatingActionButton addFAB = findViewById(R.id.mainFAB);
        addFAB.setOnClickListener(this);

        customersRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        customersRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addFAB.getVisibility() == View.VISIBLE) {
                    addFAB.hide();
                } else if (dy < 0 && addFAB.getVisibility() != View.VISIBLE) {
                    addFAB.show();
                }
            }
        });

        Objects.requireNonNull(searchTIL.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchTIL.getEditText().getWindowToken(), 0);
            showData();
            return false;
        });

        setupData();

    }

    private void showData() {
        String s = searchTIL.getEditText().getText().toString().trim();
        if (s.isEmpty()) {
            searchTIL.setVisibility(View.GONE);
        } else {
            searchTIL.setVisibility(View.VISIBLE);
        }

        ArrayList<Customer> temp = new ArrayList<>();
        for (Customer c : CustomerManager.customers) {
            if (c.search(s)) {
                temp.add(c);
            }
        }

        setAdapter(temp);
    }

    private void setupData() {
        CustomerManager.getCustomers(provider, customers -> {
            try {
                if (searchTIL.getVisibility() == View.GONE) {
                    setAdapter(customers);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private void setAdapter(ArrayList<Customer> temp) {
        CustomerManager.sort(provider, temp);
        customersRV.setAdapter(new CustomersAdapter(this, temp));
        if (temp.isEmpty()) {
            noTV.setVisibility(View.VISIBLE);
        } else {
            noTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (searchTIL.getVisibility() == View.VISIBLE) {
            searchTIL.getEditText().setText("");
            showData();
            return;
        }

        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainFAB:
                startActivity(new Intent(this, AddCustomerActivity.class));
                break;

            case R.id.searchIV:
                searchTIL.setVisibility(View.VISIBLE);
                searchTIL.getEditText().requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchTIL.getEditText(), InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.backIV:
                finish();
                break;
        }
    }

}