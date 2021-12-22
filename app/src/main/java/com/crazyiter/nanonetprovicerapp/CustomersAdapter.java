package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Customer> customers;

    public CustomersAdapter(Context context, ArrayList<Customer> customers) {
        this.context = context;
        this.customers = customers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupData(context, customers.get(position));
        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, CustomerInfoActivity.class)
                .putExtra("item", customers.get(position).getId())));
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout providerLL;
        private final TextView daysTV;
        private final TextView nameTV;
        private final TextView amountTV;

        private final ImageView noteIV;
        private final ImageView linkIV;
        private final ImageView isOnlineIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            providerLL = itemView.findViewById(R.id.customerLL);
            daysTV = itemView.findViewById(R.id.daysTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            amountTV = itemView.findViewById(R.id.amountTV);

            noteIV = itemView.findViewById(R.id.noteIV);
            linkIV = itemView.findViewById(R.id.linkIV);
            isOnlineIV = itemView.findViewById(R.id.isOnlineIV);
        }

        void setupData(Context context, Customer customer) {
            nameTV.setText(customer.getName());
            providerLL.setBackgroundResource(customer.getDiffDaysColor());
            daysTV.setText(Statics.formatNumber(customer.getDiffDays()));
            amountTV.setText(Statics.formatNumber(customer.getAmount() * 1000));

            if (customer.getFcm().isEmpty())
                isOnlineIV.setColorFilter(context.getResources().getColor(R.color.white));
            else isOnlineIV.setColorFilter(context.getResources().getColor(R.color.colorPrimary));

            if (customer.getNote() == null || customer.getNote().isEmpty()) {
                noteIV.setVisibility(View.GONE);
            }

            if (customer.isNotLinked()) {
                linkIV.setVisibility(View.GONE);
            }
        }

    }

}
