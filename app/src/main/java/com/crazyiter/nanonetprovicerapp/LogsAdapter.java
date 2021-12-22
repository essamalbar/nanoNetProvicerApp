package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.LogModel;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<LogModel> logModels;

    public LogsAdapter(Context context, ArrayList<LogModel> logModels) {
        this.context = context;
        this.logModels = logModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_customer_log, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(logModels.get(position));
    }

    @Override
    public int getItemCount() {
        return this.logModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bodyTV;
        private final TextView dateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyTV = itemView.findViewById(R.id.bodyTV);
            dateTV = itemView.findViewById(R.id.dateTV);
        }

        public void setData(LogModel data) {
            bodyTV.setText(data.getBody());
            dateTV.setText(data.getDateTime());
        }

    }
}
