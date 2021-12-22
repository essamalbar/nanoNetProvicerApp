package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.TicketModel;
import com.crazyiter.nanonetprovicerapp.db.TicketsManager;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private final Context context;
    private final String myId;
    private final TicketModel ticketModel;

    public AnswerAdapter(Context context, String myId, TicketModel ticketModel) {
        this.context = context;
        this.myId = myId;
        this.ticketModel = ticketModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_answer, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setupData(ticketModel.getAnswerModels().get(position));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if (ticketModel.getAnswerModels().get(position).getSenderId().equals(myId)) {
            holder.itemView.setBackgroundResource(R.drawable.btns_background_second);
            params.setMargins(5, 5, 100, 5);
            holder.itemView.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenu().add(0, 0, 0, R.string.delete_answer);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getOrder() == 0)
                        TicketsManager.deleteAnswer(ticketModel, ticketModel.getAnswerModels().get(position));
                    return false;
                });
                popupMenu.show();
            });
        } else {
            holder.itemView.setBackgroundResource(R.drawable.btns_background_hide);
            params.setMargins(100, 5, 5, 5);
        }
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return this.ticketModel.getAnswerModels().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView bodyTV;
        private final TextView dateTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateTimeTV = itemView.findViewById(R.id.dateTimeIV);
            this.bodyTV = itemView.findViewById(R.id.bodyTV);
        }

        public void setupData(TicketModel.TicketAnswerModel answerModel) {
            this.dateTimeTV.setText(answerModel.getDateTime());
            this.bodyTV.setText(answerModel.getBody());
        }
    }

}
