package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.crazyiter.nanonetprovicerapp.db.ProviderManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainProviderActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mainDL;
    private CardView rootCV;
    private TextView totalNumTV;
    private TextView totalNumTV2;
    private TextView totalAmountTV2;

    private TextView activeTV;
    private TextView warningTV;
    private TextView expireTV;
    private RadioGroup orderRG;
    private Provider provider;

    private LinearLayout statusLL;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_provider);

        mainDL = findViewById(R.id.mainDL);
        rootCV = findViewById(R.id.rootCV);
        totalNumTV = findViewById(R.id.totalNumTV);

        NavigationView mainNV = findViewById(R.id.mainNV);
        View navHeaderView = mainNV.getHeaderView(0);

        totalNumTV2 = navHeaderView.findViewById(R.id.totalCustomersTV);
        totalAmountTV2 = navHeaderView.findViewById(R.id.totalAmountTV);

        activeTV = navHeaderView.findViewById(R.id.activeTV);
        warningTV = navHeaderView.findViewById(R.id.warningTV);
        expireTV = navHeaderView.findViewById(R.id.expireTV);
        orderRG = navHeaderView.findViewById(R.id.orderRG);

        statusLL = navHeaderView.findViewById(R.id.statusLL);
        LinearLayout statusActionLL = navHeaderView.findViewById(R.id.statusActionLL);
        statusLL.setVisibility(View.GONE);
        statusActionLL.setOnClickListener(v -> slideAnimation(statusLL));

        LinearLayout orderActionLL = navHeaderView.findViewById(R.id.orderActionLL);
        orderRG.setVisibility(View.GONE);
        orderActionLL.setOnClickListener(v -> slideAnimation(orderRG));

        LinearLayout customersLL = findViewById(R.id.customersLL);
        customersLL.setOnClickListener(this);

        LinearLayout ticketsLL = findViewById(R.id.ticketsLL);
        ticketsLL.setOnClickListener(this);

        LinearLayout servicesLL = findViewById(R.id.servicesLL);
        servicesLL.setOnClickListener(this);

        LinearLayout plansLL = findViewById(R.id.plansLL);
        plansLL.setOnClickListener(this);

        TextView nameTV = navHeaderView.findViewById(R.id.nameTV);
        LinearLayout logoutLL = navHeaderView.findViewById(R.id.logoutLL);
        logoutLL.setOnClickListener(this);

        LinearLayout settingsLL = navHeaderView.findViewById(R.id.settingsLL);
        settingsLL.setOnClickListener(this);

        LinearLayout alertsLL = navHeaderView.findViewById(R.id.alertsLL);
        alertsLL.setOnClickListener(this);

        LinearLayout alertsSettingsLL = navHeaderView.findViewById(R.id.alertsSettingsLL);
        alertsSettingsLL.setOnClickListener(this);

        provider = Provider.getShared(this);
        nameTV.setText(provider.getName());
        orderRG.check(provider.getOrderBy());
        orderRG.setOnCheckedChangeListener((group, checkedId) -> {
            saveOrderByChecked(checkedId);
            mainDL.closeDrawer(GravityCompat.START);
        });

        ImageView menuIV = findViewById(R.id.menuIV);
        menuIV.setOnClickListener(this);

        CustomerManager.getCustomers(provider, customers -> {
            int[] dif = CustomerManager.getStatuesCount();
            activeTV.setText(Statics.formatNumber(dif[0]));
            warningTV.setText(Statics.formatNumber(dif[1]));
            expireTV.setText(Statics.formatNumber(dif[2]));

            totalNumTV.setText(Statics.formatNumber(dif[0] + dif[1] + dif[2]));
            int sumAmount = 0;
            for (Customer c : customers) {
                sumAmount += c.getAmount();
            }
            totalAmountTV2.setText(getString(R.string.total_amounts) + ": " + Statics.formatNumber(sumAmount * 1000));
            totalNumTV2.setText(getString(R.string.total_customers) + ": " + Statics.formatNumber(dif[0] + dif[1] + dif[2]));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ProviderManager.checkProvider(provider, p -> {
         /*   if (p == null || (p.getDiffDays() == 0 && p.getRemainsHours() == 0)) {
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("title", "الاشتراك منتهي")
                        .putExtra("body", "يرجى التواصل مع اﻷدمن لتجديد اشتراكك"));
                finish();
                return;
            }*/

            if (!p.getPassword().equals(provider.getPassword()) || !p.getEmail().equals(provider.getEmail())) {
                Map<String, Object> map = new HashMap<>();
                map.put("isActive", false);
                ProviderManager.update(provider, map);
                logout();
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("title", "تم تغيير اﻹعدادات")
                        .putExtra("body", "يرجى التواصل مع اﻷدمن للحصول على اﻹعدادات الجديدة"));
                finish();
                return;
            }

            if (!p.isActive()) {
                startActivity(new Intent(this, ErrorActivity.class)
                        .putExtra("title", "الحساب متوقف مؤقتاً")
                        .putExtra("body", "يرجى التواصل مع اﻷدمن ﻹعادة تفعيل الحساب"));
                finish();
            }

        });
    }

    private void slideAnimation(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void saveOrderByChecked(int checkedId) {
        provider.setOrderBy(checkedId);
        provider.saveShared(this);
        Map<String, Object> map = new HashMap<>();
        map.put("orderBy", checkedId);
        ProviderManager.update(provider, map);
    }

    @Override
    public void onBackPressed() {
        if (mainDL.isDrawerOpen(GravityCompat.START)) {
            mainDL.closeDrawer(GravityCompat.START);
            return;
        }

        super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuIV:
                mainDL.openDrawer(GravityCompat.START);
                break;

            case R.id.customersLL:
                startActivity(new Intent(this, CustomersActivity.class));
                break;

            case R.id.ticketsLL:
                startActivity(new Intent(this, TicketsActivity.class));
                break;

            case R.id.servicesLL:
                startActivity(new Intent(this, ServicesActivity.class));
                break;

            case R.id.plansLL:
                startActivity(new Intent(this, PlansActivity.class));
                break;

            case R.id.settingsLL:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.alertsLL:
                startActivity(new Intent(this, AlertsActivity.class));
                break;

            case R.id.alertsSettingsLL:
                startActivity(new Intent(this, AlertsSettingsActivity.class));
                break;

            case R.id.logoutLL:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sure_logout))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            logout();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        }).create().show();
                break;
        }
    }

    private void logout() {
        ProviderManager.logout(provider);
        FirebaseFirestore.getInstance().clearPersistence();
        Provider.logout(this);
    }

}