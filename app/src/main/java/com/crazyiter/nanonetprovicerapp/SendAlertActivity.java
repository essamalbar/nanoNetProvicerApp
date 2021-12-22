package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.cardview.widget.CardView;

import com.crazyiter.nanonetprovicerapp.db.AlertManager;
import com.crazyiter.nanonetprovicerapp.db.AlertModel;
import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;
import com.crazyiter.nanonetprovicerapp.db.Provider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SendAlertActivity extends AppCompatActivity {

    private TextView smsCheckTV;
    private TextView alertCustomersTV;
    private TextView menuTV;
    private ImageView menuIV;
    private LinearLayout menuLL;
    private List<CustomMenuItem> items;
    private final ArrayList<Customer> selectedCustomers = new ArrayList<>();
    private ArrayList<Customer> base;

    private CardView rootCV;
    private RadioGroup typeRG;
    private TextInputLayout messageTIL;

    private int selectedMenuItemPosition;
    private static final int customPosition = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_alert);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        rootCV = findViewById(R.id.rootCV);
        typeRG = findViewById(R.id.typeRG);
        messageTIL = findViewById(R.id.messageTIL);
        Button sendBTN = findViewById(R.id.sendBTN);

        smsCheckTV = findViewById(R.id.smsCheckTV);
        alertCustomersTV = findViewById(R.id.alertCustomersTV);
        menuLL = findViewById(R.id.menuItemLL);
        menuTV = findViewById(R.id.menuItemTV);
        menuIV = findViewById(R.id.menuItemIV);

        typeRG.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.smsRB) smsCheckTV.setVisibility(View.VISIBLE);
            else smsCheckTV.setVisibility(View.GONE);

            filterSMSApp();
        });

        showPopupMenu();
        checkCount(0);

        sendBTN.setOnClickListener(v -> {
            if (selectedCustomers.isEmpty()) {
                Statics.showSnackBar(rootCV, "الرجاء تحديد مشتركين");
                return;
            }

            String msg = messageTIL.getEditText().getText().toString().trim();
            if (msg.isEmpty()) {
                Statics.showSnackBar(rootCV, "الرجاء كتابة رسالة تنبيه");
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("تأكيد اﻹرسال")
                    .setMessage("الرسالة: " + msg)
                    .setPositiveButton(getString(R.string.send), (dialog, which) -> {
                        if (typeRG.getCheckedRadioButtonId() == R.id.smsRB) {
                            List<String> numbers = new ArrayList<>();
                            for (Customer c : selectedCustomers) numbers.add(c.getMobile());
                            Statics.sendSMS(this, numbers, msg);
                            finish();
                        } else {
                            sendBTN.setOnClickListener(null);
                            AlertModel alertModel = new AlertModel("", Provider.getShared(this).getId(), msg, Statics.LocalDate.getCurrentDate(), false, items.get(selectedMenuItemPosition).getImageRes(), items.get(selectedMenuItemPosition).getColorTint());
                            for (Customer c : selectedCustomers) alertModel.addCustomer(c);
                            AlertManager.addAlert(alertModel);
                            try {
                                VolleyManager.sendNotification(alertModel);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Snackbar snackbar = Statics.showSnackBar(rootCV, "تم إرسال الاشعار بنجاح");
                            snackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);
                                    finish();
                                }
                            });
                        }
                    }).create().show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void filterSMSApp() {
        selectedCustomers.clear();
        for (Customer c : base) {
            if (addCustomer(c, selectedMenuItemPosition)) {
                selectedCustomers.add(c);
            }
        }
        alertCustomersTV.setText(getString(R.string.customers) + ": " + selectedCustomers.size());
    }

    private void showPopupMenu() {
        final ListPopupWindow popupWindow = new ListPopupWindow(this);
        items = new ArrayList<>();
        items.add(new CustomMenuItem("الكل", R.drawable.ic_round_check_box, R.color.colorPrimary));
        items.add(new CustomMenuItem("الاشتراكات الفعالة", R.drawable.ic_baseline_circle, R.color.colorGreen));
        items.add(new CustomMenuItem("الاشتراكات التي على وشك الانتهاء", R.drawable.ic_baseline_circle, R.color.colorYellow));
        items.add(new CustomMenuItem("الاشتراكات المنتهية", R.drawable.ic_baseline_circle, R.color.colorRed));
        items.add(new CustomMenuItem("لكل مشترك ديونه أكبر من 0", R.drawable.ic_round_keyboard_capslock, R.color.colorPrimary));
        items.add(new CustomMenuItem("لكل مشترك ديونه تساوي 0", R.drawable.ic_round_thumb_up, R.color.colorGreen));
        items.add(new CustomMenuItem("مخصص", R.drawable.ic_round_people, R.color.colorRed));

        ListAdapter adapter = new ListPopupWindowAdapter(this, items);
        popupWindow.setAnchorView(menuIV);
        popupWindow.setWidth(-1);
        popupWindow.setAdapter(adapter);
        popupWindow.setOnItemClickListener((parent, view, position, id) -> {
            checkCount(position);
            popupWindow.dismiss();
        });

        menuLL.setOnClickListener(v -> popupWindow.show());
    }

    @SuppressLint("SetTextI18n")
    private void checkCount(int position) {
        selectedMenuItemPosition = position;
        CustomMenuItem selectedMenuItem = items.get(position);
        menuTV.setText(selectedMenuItem.getTitle());
        menuIV.setImageResource(selectedMenuItem.getImageRes());
        menuIV.setColorFilter(getResources().getColor(selectedMenuItem.getColorTint()));

        if (position == customPosition) {
            new SelectCustomersBottomSheetDialogFragment(customers -> {
                base = customers;
                filterSMSApp();
            }).show(getSupportFragmentManager());
            return;
        }

        base = CustomerManager.customers;
        filterSMSApp();
    }

    private boolean addCustomer(Customer c, int position) {
        if (typeRG.getCheckedRadioButtonId() == R.id.smsRB && c.getMobile().isEmpty()) return false;
        if (position == 0) return true;
        if (position == 1) return c.getDiffDaysColor() == R.color.colorGreen;
        if (position == 2) return c.getDiffDaysColor() == R.color.colorYellow;
        if (position == 3) return c.getDiffDaysColor() == R.color.colorRed;
        if (position == 4) return c.getAmount() > 0;
        if (position == 5) return c.getAmount() == 0;
        return true;
    }

}