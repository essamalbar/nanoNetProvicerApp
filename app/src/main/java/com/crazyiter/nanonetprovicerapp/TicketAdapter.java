package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.TicketModel;
import com.crazyiter.nanonetprovicerapp.db.TicketsManager;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private final AppCompatActivity activity;
    private final ArrayList<TicketModel> ticketModels;

    public TicketAdapter(AppCompatActivity activity, ArrayList<TicketModel> ticketModels) {
        this.activity = activity;
        this.ticketModels = ticketModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(activity)
                        .inflate(R.layout.item_ticket, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupData(activity, ticketModels.get(position));
        holder.getItemLL().setOnClickListener(v -> new TicketAnswersBottomDialog(ticketModels.get(position)).show(activity.getSupportFragmentManager()));
        holder.getItemLL().setOnLongClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(activity, v);
            popupMenu.getMenu().add(0, 0, 0, R.string.delete_ticket);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getOrder() == 0)
                    TicketsManager.deleteTicket(ticketModels.get(position));
                return false;
            });
            popupMenu.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return this.ticketModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout itemLL;
        private final TextView nameTV;
        private final TextView bodyTV;
        private final TextView dateTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemLL = itemView.findViewById(R.id.ticketLL);
            this.nameTV = itemView.findViewById(R.id.nameTV);
            this.dateTimeTV = itemView.findViewById(R.id.dateTimeIV);
            this.bodyTV = itemView.findViewById(R.id.bodyTV);
        }

        public void setupData(Context context, TicketModel ticketModel) {
            this.dateTimeTV.setText(ticketModel.getDateTime());
            this.bodyTV.setText(ticketModel.getBody());
            CustomerManager.getCustomer(ticketModel.getCustomerId(), c -> this.nameTV.setText(c.getName()));

            if (!ticketModel.isActive()) {
                this.itemLL.setBackgroundResource(R.color.colorRedLight);
            } else {
                if (ticketModel.getAnswerModels().isEmpty()) {
                    this.itemLL.setBackgroundResource(R.color.colorGreenLight);
                } else {
                    TicketModel.TicketAnswerModel answerModel = ticketModel.getAnswerModels().get(ticketModel.getAnswerModels().size() - 1);
                    if (!answerModel.getSenderId().equals(Provider.getShared(context).getId())) {
                        this.itemLL.setBackgroundResource(R.color.colorYellowLight);
                    }
                }
            }
        }

        public LinearLayout getItemLL() {
            return itemLL;
        }
    }

}
