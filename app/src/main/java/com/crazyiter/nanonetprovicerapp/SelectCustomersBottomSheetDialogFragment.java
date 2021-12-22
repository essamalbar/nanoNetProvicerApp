package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SelectCustomersBottomSheetDialogFragment extends BottomSheetDialogFragment implements SelectCustomersAdapter.SelectCustomerListener {

    private TextInputLayout searchTIL;
    private ImageView searchIV;
    private RecyclerView customersRV;
    private TextView noTV;
    private Provider provider;

    private ArrayList<Customer> selectedCustomers;
    private Button doneBTN;
    private final SelectCustomersAdapter.SelectCustomerListener selectCustomerListener;

    public SelectCustomersBottomSheetDialogFragment(SelectCustomersAdapter.SelectCustomerListener selectCustomerListener) {
        this.selectCustomerListener = selectCustomerListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialog_select_customers, container, false);

        provider = Provider.getShared(requireContext());

        searchIV = view.findViewById(R.id.searchIV);
        searchTIL = view.findViewById(R.id.searchTIL);
        noTV = view.findViewById(R.id.noTV);
        doneBTN = view.findViewById(R.id.doneBTN);

        customersRV = view.findViewById(R.id.customersRV);
        customersRV.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchIV.setOnClickListener(v -> {
            searchTIL.setVisibility(View.VISIBLE);
            Objects.requireNonNull(searchTIL.getEditText()).requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchTIL.getEditText(), InputMethodManager.SHOW_IMPLICIT);
        });

        Objects.requireNonNull(searchTIL.getEditText()).setOnEditorActionListener((v, actionId, event) -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchTIL.getEditText().getWindowToken(), 0);
            showData();
            return false;
        });

        searchTIL.setEndIconOnClickListener(v -> {
            searchTIL.getEditText().setText("");
            showData();
        });

        doneBTN.setOnClickListener(v -> {
            selectCustomerListener.onSelect(selectedCustomers);
            dismiss();
        });

        showData();
    }

    private void showData() {
        String s = Objects.requireNonNull(searchTIL.getEditText()).getText().toString().trim();
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

    private void setAdapter(ArrayList<Customer> temp) {
        CustomerManager.sort(provider, temp);
        customersRV.setAdapter(new SelectCustomersAdapter(requireContext(), temp, this));
        if (temp.isEmpty()) {
            noTV.setVisibility(View.VISIBLE);
        } else {
            noTV.setVisibility(View.GONE);
        }
    }

    public void show(@NonNull FragmentManager manager) {
        super.show(manager, getTag());
    }

    @Override
    public void onSelect(ArrayList<Customer> customers) {
        selectedCustomers = customers;
    }

}
