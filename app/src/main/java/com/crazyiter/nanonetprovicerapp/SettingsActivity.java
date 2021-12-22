package com.crazyiter.nanonetprovicerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS_SHARED_NAME = "nanonet_provider_settings";
    public static final String DURATION = "duration";
    public static final String TIME = "time";
    public static boolean dateOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(v -> onBackPressed());

        RadioGroup durationRG = findViewById(R.id.durationRG);
        RadioGroup dateTimeRG = findViewById(R.id.dateTimeRG);

        durationRG.check(getShared(this, DURATION, R.id.monthRB));
        dateTimeRG.check(getShared(this, TIME, R.id.dateTimeRB));

        durationRG.setOnCheckedChangeListener((group, checkedId) -> saveShared(DURATION, checkedId));
        dateTimeRG.setOnCheckedChangeListener((group, checkedId) -> {
            saveShared(TIME, checkedId);
            setupDate(this);
        });

    }

    private void saveShared(String name, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_SHARED_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int getShared(Context context, String name, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SETTINGS_SHARED_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, defaultValue);
    }

    public static int getDurationDays(Context context, String startDate) {
        int id = getShared(context, DURATION, R.id.monthRB);
        if (id == R.id.month30RB) {
            return 30;
        }

        String endDate = Statics.LocalDate.getEndDateAddMonths(startDate, 1);
        return Statics.LocalDate.getDifferenceDays(startDate, endDate);
    }

    public static void setupDate(Context context) {
        int dateTime = getShared(context, TIME, R.id.dateTimeRB);
        dateOnly = (dateTime == R.id.dateRB);
    }

}