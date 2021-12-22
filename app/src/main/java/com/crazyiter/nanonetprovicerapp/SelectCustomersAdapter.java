package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.Customer;

import java.util.ArrayList;

public class SelectCustomersAdapter extends RecyclerView.Adapter<SelectCustomersAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Customer> customers;
    private final ArrayList<Customer> selectedCustomers;
    private final SelectCustomerListener selectCustomerListener;

    public SelectCustomersAdapter(Context context, ArrayList<Customer> customers, SelectCustomerListener selectCustomerListener) {
        this.context = context;
        this.customers = customers;
        this.selectCustomerListener = selectCustomerListener;
        this.selectedCustomers = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_customer, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupData(customers.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (v.isSelected()) {
                selectedCustomers.remove(customers.get(position));
                v.setSelected(false);
            } else {
                selectedCustomers.add(customers.get(position));
                v.setSelected(true);
            }

            selectCustomerListener.onSelect(selectedCustomers);

        });
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }


    public interface SelectCustomerListener {
        void onSelect(ArrayList<Customer> customers);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout providerLL;
        private final TextView daysTV;
        private final TextView nameTV;
        private final TextView amountTV;

        private final ImageView noteIV;
        private final ImageView linkIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            providerLL = itemView.findViewById(R.id.customerLL);
            daysTV = itemView.findViewById(R.id.daysTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            amountTV = itemView.findViewById(R.id.amountTV);

            noteIV = itemView.findViewById(R.id.noteIV);
            linkIV = itemView.findViewById(R.id.linkIV);
        }

        @SuppressLint("NonConstantResourceId")
        void setupData(Customer customer) {
            int i = R.drawable.item_background_selector_green;
            switch (customer.getDiffDaysColor()) {
                case R.color.colorGreen:
                    i = R.drawable.item_background_selector_green;
                    break;

                case R.color.colorYellow:
                    i = R.drawable.item_background_selector_yellow;
                    break;

                case R.color.colorRed:
                    i = R.drawable.item_background_selector_red;
                    break;
            }
            providerLL.setBackgroundResource(i);

            nameTV.setText(customer.getName());
            daysTV.setText(Statics.formatNumber(customer.getDiffDays()));
            amountTV.setText(Statics.formatNumber(customer.getAmount() * 1000));

            if (customer.getNote() == null || customer.getNote().isEmpty()) {
                noteIV.setVisibility(View.GONE);
            }

            if (customer.isNotLinked()) {
                linkIV.setVisibility(View.GONE);
            }
        }

    }

}
