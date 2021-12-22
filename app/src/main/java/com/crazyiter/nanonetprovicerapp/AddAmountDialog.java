package com.crazyiter.nanonetprovicerapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.LogManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.textfield.TextInputLayout;

public class AddAmountDialog extends Dialog {

    private final Customer customer;
    private Provider provider;
    private TextInputLayout amountTIL;

    public AddAmountDialog(@NonNull Context context, Customer customer) {
        super(context);
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_amount);

        provider = Provider.getShared(getContext());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        amountTIL = findViewById(R.id.amountTIL);
        Button saveBTN = findViewById(R.id.addBTN);

        saveBTN.setOnClickListener(v -> {
            String amount = amountTIL.getEditText().getText().toString();
            boolean b = Statics.checkInput(amountTIL, getContext().getString(R.string.enter_amount));
            if (b) {
                int i = customer.getAmount() + Integer.parseInt(amount);
                customer.setAmount(i);
                customer.addLog(LogManager.addAmount(provider, Integer.parseInt(amount), customer));
                CustomerManager.updateCustomer(customer);
                dismiss();
            }

        });

    }

}
