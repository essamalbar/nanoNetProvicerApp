package com.crazyiter.nanonetprovicerapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class NoteDialog extends Dialog implements TextWatcher {

    private Customer customer;
    private TextInputLayout noteTIL;
    private Button saveBTN;

    public NoteDialog(@NonNull Context context, Customer customer) {
        super(context);
        this.customer = customer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_note);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        noteTIL = findViewById(R.id.noteTIL);
        saveBTN = findViewById(R.id.saveBTN);
        noteTIL.getEditText().setText(customer.getNote());

        Objects.requireNonNull(noteTIL.getEditText()).addTextChangedListener(this);
        saveBTN.setOnClickListener(v -> {
            customer.setNote(noteTIL.getEditText().getText().toString().trim());
            CustomerManager.updateCustomer(customer);
            dismiss();
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        saveBTN.setBackgroundResource(R.drawable.btns_background);
    }
}
