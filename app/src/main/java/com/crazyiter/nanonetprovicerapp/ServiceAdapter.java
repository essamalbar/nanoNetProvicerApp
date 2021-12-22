package com.crazyiter.nanonetprovicerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.crazyiter.nanonetprovicerapp.db.ServiceManager;
import com.crazyiter.nanonetprovicerapp.db.ServiceModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.ionbit.ionalert.IonAlert;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<ServiceModel> serviceModels;

    public ServiceAdapter(Context context, ArrayList<ServiceModel> serviceModels) {
        this.context = context;
        this.serviceModels = serviceModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.item_service, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceModel model = serviceModels.get(position);
        holder.setupData(model);

        holder.itemView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.getMenu().add(0, 0, 0, context.getString(R.string.delete));
            popupMenu.getMenu().add(0, 1, 0, context.getString(R.string.go_link));
            popupMenu.getMenu().add(0, 1, 1, "نعديل");
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 0:
                       /* new AlertDialog.Builder(context)
                                .setMessage(R.string.sure_delete)
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.confirm, (dialog, w) -> {
                                    ServiceManager.deleteService(model);
                                })
                                .create()
                                .show();*/
                        new IonAlert(context, IonAlert.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.sure_delete))
                                .setCancelText(context.getString(R.string.cancel))
                                .setConfirmText(context.getString(R.string.confirm))
                                .showCancelButton(true)
                                .setCancelClickListener(new IonAlert.ClickListener() {
                                    @Override
                                    public void onClick(IonAlert sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .setConfirmClickListener(new IonAlert.ClickListener() {
                                    @Override
                                    public void onClick(IonAlert ionAlert) {
                                        ServiceManager.deleteService(model);
                                    }
                                })
                                .show();
                        return true;

                    case 1:
                        Statics.openBrowser(context, model.getUrl());
                        return true;
                    case 2:
S
                        return true;
                }


                return false;
            });
            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView serviceIV;
        private final TextView serviceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceIV = itemView.findViewById(R.id.serviceIV);
            serviceTV = itemView.findViewById(R.id.serviceTV);
        }

        private void setupData(ServiceModel serviceModel) {
            serviceTV.setText(serviceModel.getName());
            Picasso.get()
                    .load(serviceModel.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(serviceIV);
        }

    }
}
