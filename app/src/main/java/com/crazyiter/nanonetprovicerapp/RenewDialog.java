package com.crazyiter.nanonetprovicerapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.LogManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.textfield.TextInputLayout;

public class RenewDialog extends Dialog {

    private final Customer customer;
    private Provider provider;
    private TextInputLayout costTIL, dateTIL;
    private CheckBox getCB;
    private String currentDateTime;

    public RenewDialog(@NonNull Context context, Customer customer) {
        super(context);
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_renew);

        provider = Provider.getShared(getContext());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        currentDateTime = Statics.LocalDate.getCurrentDate();

        costTIL = findViewById(R.id.costTIL);
        dateTIL = findViewById(R.id.dateTIL);
        getCB = findViewById(R.id.getCB);
        Button saveBTN = findViewById(R.id.saveBTN);

        dateTIL.getEditText().setText(currentDateTime);
        dateTIL.setStartIconOnClickListener(v -> Statics.LocalDate.getDate(getContext(), date -> {
            currentDateTime = date;
            dateTIL.getEditText().setText(date);
        }));

        saveBTN.setOnClickListener(v -> {
            String date = currentDateTime;
            String cost  = costTIL.getEditText().getText().toString();

            boolean b = Statics.checkInput(costTIL, getContext().getString(R.string.enter_cost));
            if (b) {
                int days = customer.getDiffDays();
                customer.setCost(Integer.parseInt(cost));
                customer.setStartDate(date);
                customer.setDays(days + SettingsActivity.getDurationDays(getContext(), date));

                if (!getCB.isChecked()) {
                    int i = customer.getAmount() + Integer.parseInt(cost);
                    customer.setAmount(i);
                }

                customer.addLog(LogManager.renew(provider, getCB.isChecked(), Integer.parseInt(cost), customer));
                CustomerManager.updateCustomer(customer);
                dismiss();
            }

        });

    }

}
