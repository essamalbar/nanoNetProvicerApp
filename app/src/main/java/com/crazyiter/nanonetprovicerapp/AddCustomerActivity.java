package com.crazyiter.nanonetprovicerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.LogManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.textfield.TextInputLayout;

public class AddCustomerActivity extends AppCompatActivity {

    private TextInputLayout nameTIL;
    private TextInputLayout mobileTIL;
    private TextInputLayout userTIL;
    private TextInputLayout amountTIL;
    private TextInputLayout costTIL;
    private TextInputLayout dateTIL;

    private TextInputLayout nanoUrlTIL;
    private LinearLayout nanoLL;
    private TextInputLayout nanoUsernameTIL;
    private TextInputLayout nanoPasswordTIL;

    private CheckBox getCB;
    private Provider provider;
    private Customer customer;

    private CardView rootCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        provider = Provider.getShared(this);

        rootCV = findViewById(R.id.rootCV);
        nameTIL = findViewById(R.id.nameTIL);
        mobileTIL = findViewById(R.id.mobileTIL);
        userTIL = findViewById(R.id.userTIL);
        amountTIL = findViewById(R.id.amountTIL);
        costTIL = findViewById(R.id.costTIL);

        nanoUrlTIL = findViewById(R.id.nanoUrlTIL);
        nanoLL = findViewById(R.id.nanoLL);
        nanoUsernameTIL = findViewById(R.id.nanoUsernameTIL);
        nanoPasswordTIL = findViewById(R.id.nanoPasswordTIL);
        nanoUrlTIL.setEndIconOnClickListener(v -> {
            if (nanoLL.getVisibility() == View.GONE) {
                nanoLL.setVisibility(View.VISIBLE);
            } else {
                nanoLL.setVisibility(View.GONE);
            }
        });

        dateTIL = findViewById(R.id.dateTIL);
        dateTIL.getEditText().setText(Statics.LocalDate.getCurrentDate());
        dateTIL.setStartIconOnClickListener(v -> Statics.LocalDate.getDate(this, date -> {
            dateTIL.getEditText().setText(date);
        }));

        getCB = findViewById(R.id.getCB);

        try {
            String customerId = getIntent().getStringExtra("item");
            CustomerManager.getCustomer(customerId, c -> {
                customer = c;
                nameTIL.getEditText().setText(customer.getName());
                mobileTIL.getEditText().setText(customer.getMobile());
                userTIL.getEditText().setText(customer.getUser());
                amountTIL.getEditText().setText(Statics.formatNumber(customer.getAmount()));
                costTIL.getEditText().setText(Statics.formatNumber(customer.getCost()));
                dateTIL.getEditText().setText(customer.getStartDate());

                nanoUrlTIL.getEditText().setText(customer.getNanoUrl());
                nanoUsernameTIL.getEditText().setText(customer.getNanoUsername());
                nanoPasswordTIL.getEditText().setText(customer.getNanoPassword());

                getCB.setVisibility(View.GONE);
                amountTIL.setVisibility(View.GONE);
            });
        } catch (Exception ignored) {
        }

        Button saveBTN = findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(v -> {
            boolean b = Statics.checkInput(nameTIL, getString(R.string.enter_name)) &&
                    Statics.checkInput(costTIL, getString(R.string.enter_cost));

            if (b) {
                String name = nameTIL.getEditText().getText().toString().trim();
                String mobile = mobileTIL.getEditText().getText().toString().trim();
                String user = userTIL.getEditText().getText().toString().trim();
                String amount = amountTIL.getEditText().getText().toString().trim();
                String cost = costTIL.getEditText().getText().toString().trim();
                String startDate = dateTIL.getEditText().getText().toString().trim();

                String nanoUrl = nanoUrlTIL.getEditText().getText().toString().trim();
                String nanoUsername = nanoUsernameTIL.getEditText().getText().toString().trim();
                String nanoPassword = nanoPasswordTIL.getEditText().getText().toString().trim();

                int amount2;
                if (amount.isEmpty()) {
                    amount = "0";
                    amount2 = 0;
                } else {
                    amount2 = Integer.parseInt(amount);
                }

                if (cost.isEmpty()) {
                    cost = "0";
                }

                if (customer == null) {
                    if (!getCB.isChecked()) {
                        amount2 += Integer.parseInt(cost);
                    }

                    customer = new Customer("", name, user, mobile, startDate, Integer.parseInt(cost), provider.getId(), amount2, nanoUrl, nanoUsername, nanoPassword, "", "", "", SettingsActivity.getDurationDays(this, startDate), "");
                    if (CustomerManager.existCount(customer) == 0) {
                        customer.addLog(LogManager.renew(provider, getCB.isChecked(), Integer.parseInt(cost), customer));
                        customer.addLog(LogManager.addAmount(provider, Integer.parseInt(amount), customer));
                        CustomerManager.addCustomer(customer);
                        finish();
                    } else {
                        Statics.showSnackBar(rootCV, getString(R.string.exist));
                    }
                } else {
                    customer.setName(name);
                    customer.setMobile(mobile);
                    customer.setUser(user);
                    customer.setAmount(amount2);
                    customer.setCost(Integer.parseInt(cost));
                    customer.setStartDate(startDate);
                    customer.setNanoUrl(nanoUrl);
                    customer.setNanoUsername(nanoUsername);
                    customer.setNanoPassword(nanoPassword);

                    if (CustomerManager.existCount(customer) == 0) {
                        CustomerManager.updateCustomer(customer);
                        finish();
                    } else {
                        Statics.showSnackBar(rootCV, getString(R.string.exist));
                    }
                }
            }

        });

        setupDate();
    }

    private void setupDate() {

    }
}