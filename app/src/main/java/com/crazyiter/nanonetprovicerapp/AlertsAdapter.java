package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.AlertManager;
import com.crazyiter.nanonetprovicerapp.db.AlertModel;

import java.util.ArrayList;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<AlertModel> alertModels;

    public AlertsAdapter(Context context, ArrayList<AlertModel> alertModels) {
        this.context = context;
        this.alertModels = alertModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_alert, parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlertModel item = alertModels.get(position);
        holder.getMessageTV().setText(item.getMessage());
        holder.getDateTV().setText(item.getDateTime() + "\n" + "إلى: " + item.getCustomerIds().length());

        holder.getTypeIV().setImageResource(item.getTypeIconRes());
        holder.getTypeIV().setColorFilter(context.getResources().getColor(item.getTypeIconColor()));

        holder.getRemoveIV().setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.delete))
                .setMessage(item.getMessage())
                .setPositiveButton(context.getString(R.string.confirm), (dialog, which) -> AlertManager.deleteAlert(item)).create().show());
    }

    @Override
    public int getItemCount() {
        return alertModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView removeIV;
        private final ImageView typeIV;
        private final TextView messageTV;
        private final TextView dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.removeIV = itemView.findViewById(R.id.removeIV);
            this.typeIV = itemView.findViewById(R.id.typeIV);
            this.messageTV = itemView.findViewById(R.id.messageTV);
            this.dateTV = itemView.findViewById(R.id.dateTV);
        }

        public ImageView getTypeIV() {
            return typeIV;
        }

        public ImageView getRemoveIV() {
            return removeIV;
        }

        public TextView getMessageTV() {
            return messageTV;
        }

        public TextView getDateTV() {
            return dateTV;
        }
    }
}
