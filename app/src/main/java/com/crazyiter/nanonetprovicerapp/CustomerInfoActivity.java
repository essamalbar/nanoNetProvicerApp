package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerInfoActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView nameTV, userTV, mobileTV, startTV, endTV, daysTV, amountTV, costTV;
    private Customer customer;
    private ImageView moreIV;
    private LinearLayout mobileLL;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        nameTV = findViewById(R.id.nameTV);
        amountTV = findViewById(R.id.amountTV);
        costTV = findViewById(R.id.costTV);
        userTV = findViewById(R.id.userTV);
        mobileTV = findViewById(R.id.mobileTV);
        startTV = findViewById(R.id.startDateTV);
        endTV = findViewById(R.id.endDateTV);
        daysTV = findViewById(R.id.daysTV);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());
        moreIV = findViewById(R.id.moreIV);
        moreIV.setOnClickListener(v -> showPopupMenu());

        ImageView noteIV = findViewById(R.id.noteIV);
        noteIV.setOnClickListener(v -> new NoteDialog(this, customer).show());

        ImageView linkIV = findViewById(R.id.linkIV);
        linkIV.setOnClickListener(v -> new CustomerAccountDialog(this, customer).show());

        mobileLL = findViewById(R.id.mobileLL);
        mobileLL.setOnClickListener(v -> Statics.makeCall(this, customer.getMobile()));

        bottomNavigationView = findViewById(R.id.customerBNV);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        setupData();
    }

    private void showPopupMenu() {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        List<CustomMenuItem> itemList = new ArrayList<>();
        itemList.add(new CustomMenuItem(getString(R.string.share), R.drawable.ic_round_share));
        itemList.add(new CustomMenuItem(getString(R.string.log), R.drawable.ic_round_history));
        itemList.add(new CustomMenuItem(getString(R.string.add_amount), R.drawable.ic_round_monetization_on));
        itemList.add(new CustomMenuItem(getString(R.string.pay), R.drawable.ic_round_attach_money));
        ListAdapter adapter = new ListPopupWindowAdapter(this, itemList);
        popupWindow.setAnchorView(moreIV);
        popupWindow.setWidth((int) (nameTV.getWidth() * 0.75));
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Statics.shareText(this, customer.toString());
                    break;

                case 1:
                    startActivity(new Intent(this, CustomerLogActivity.class)
                            .putExtra("item", customer.getId()));
                    break;

                case 2:
                    new AddAmountDialog(this, customer).show();
                    break;

                case 3:
                    new PayDialog(this, customer).show();
                    break;
            }
            popupWindow.dismiss();
        });
        popupWindow.show();
    }

    @SuppressLint("SetTextI18n")
    private void setupData() {
        String customerId = getIntent().getStringExtra("item");
        CustomerManager.getCustomer(customerId, c -> {
            if (c == null) {
                finish();
            } else {
                customer = c;
            }

            nameTV.setText(customer.getName());
            amountTV.setText(Statics.formatNumber(customer.getAmount() * 1000));
            userTV.setText(customer.getUser());
            mobileTV.setText(customer.getMobile());
            if (customer.getMobile().isEmpty()) {
                mobileLL.setVisibility(View.GONE);
            } else {
                mobileLL.setVisibility(View.VISIBLE);
            }
            costTV.setText(Statics.formatNumber(customer.getCost() * 1000));
            startTV.setText(Statics.LocalDate.formatDateOnly(customer.getStartDate()));
            endTV.setText(Statics.LocalDate.formatDateOnly(customer.getEndDate()));

            int dif = customer.getDiffDaysColor();
            daysTV.setTextColor(getResources().getColor(dif));
            if (dif == R.color.colorRed) {
                daysTV.setText(getString(R.string.expired));
            } else {
                if (SettingsActivity.dateOnly) {
                    daysTV.setText(customer.getDiffDays() + " يوم");
                } else {
                    daysTV.setText(customer.getDiffDays() + " يوم و" + customer.getRemainsHours() + " ساعة");
                }
            }

            MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.pause);
            if (customer.isActive()) {
                menuItem.setIcon(R.drawable.ic_round_pause);
                menuItem.setTitle("تعطيل");
            } else {
                menuItem.setIcon(R.drawable.ic_round_play);
                menuItem.setTitle("إعادة تفعيل");
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, AddCustomerActivity.class)
                        .putExtra("item", customer.getId()));
                return true;

            case R.id.renew:
                if (customer.getDiffDays() > 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("يوجد اشتراك حالي!")
                            .setMessage("تأكيد إضافة اشتراك جديد")
                            .setPositiveButton(R.string.confirm, (dialog, which) -> new RenewDialog(this, customer).show()).create().show();
                } else {
                    new RenewDialog(this, customer).show();
                }
                return true;

            case R.id.pause:
                String msg;
                if (customer.isActive()) msg = getString(R.string.sure_stop);
                else msg = getString(R.string.sure_active);

                new AlertDialog.Builder(this)
                        .setMessage(msg)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("isActive", !customer.isActive());
                            CustomerManager.updateCustomer(customer, map);
                        }).create().show();
                return true;

            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.sure_delete))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), (dialog, which) -> {
                            CustomerManager.deleteCustomer(customer);
                            finish();
                        }).create().show();
                return true;
        }
        return false;
    }
}