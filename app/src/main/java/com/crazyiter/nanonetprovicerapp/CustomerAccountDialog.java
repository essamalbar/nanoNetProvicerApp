package com.crazyiter.nanonetprovicerapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class CustomerAccountDialog extends Dialog {

    private final Customer customer;
    private TextInputLayout emailTIL, passwordTIL;
    private LinearLayout rootLL;

    public CustomerAccountDialog(@NonNull Context context, Customer customer) {
        super(context);
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_customer_account);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        emailTIL = findViewById(R.id.usernameTIL);
        passwordTIL = findViewById(R.id.passwordTIL);
        rootLL = findViewById(R.id.rootLL);

        Objects.requireNonNull(emailTIL.getEditText()).setText(customer.getEmail());
        Objects.requireNonNull(passwordTIL.getEditText()).setText(customer.getPassword());

        Button saveBTN = findViewById(R.id.saveBTN);
        saveBTN.setOnClickListener(v -> {
            String email = emailTIL.getEditText().getText().toString().trim();
            String password = passwordTIL.getEditText().getText().toString().trim();

            customer.setEmail(email);
            customer.setPassword(password);

            int i = CustomerManager.existCount(customer);
            if (i > 0) {
                Statics.showSnackBar(rootLL, getContext().getString(R.string.exist));
            } else {
                CustomerManager.updateCustomer(customer);
                dismiss();
            }
        });

    }
}
