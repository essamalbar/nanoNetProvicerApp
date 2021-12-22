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

public class PayDialog extends Dialog {

    private final Customer customer;
    private Provider provider;
    private TextInputLayout cashTIL, dateTIL;

    public PayDialog(@NonNull Context context, Customer customer) {
        super(context);
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);

        provider = Provider.getShared(getContext());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        cashTIL = findViewById(R.id.cashTIL);
        dateTIL = findViewById(R.id.dateTIL);
        dateTIL.setStartIconOnClickListener(v -> Statics.LocalDate.getDate(getContext(), date -> dateTIL.getEditText().setText(date)));
        dateTIL.getEditText().setText(Statics.LocalDate.getCurrentDate());

        Button saveBTN = findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(v -> {
            String cash = cashTIL.getEditText().getText().toString();
            int cash2;
            if (cash.isEmpty()) {
                cash2 = 0;
            } else {
                cash2 = Integer.parseInt(cash);
            }

            if (cash2 > customer.getAmount()) {
                cashTIL.setError(getContext().getString(R.string.cash_bigger));
                return;
            }

            int i = customer.getAmount() - cash2;
            customer.setAmount(i);

            customer.addLog(LogManager.payAmount(provider, cash2, customer));
            CustomerManager.updateCustomer(customer);
            dismiss();

        });

    }

}
