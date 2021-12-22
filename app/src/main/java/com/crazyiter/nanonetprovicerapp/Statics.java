package com.crazyiter.nanonetprovicerapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Statics {

    public static Snackbar showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundResource(R.color.colorGreen);
        snackbar.show();
        return snackbar;
    }

    @SuppressLint("DefaultLocale")
    public static String formatNumber(int num) {
        try {
            return String.format(Locale.ENGLISH, "%,d", Long.parseLong(String.valueOf(num)));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean checkInput(TextInputLayout layout, String errorMessage) {
        if (layout.getEditText().getText().toString().trim().isEmpty()) {
            layout.setError(errorMessage);
            return false;
        }

        layout.setError("");
        return true;
    }

    public static boolean checkInput(TextInputLayout layout, String errorMessage, boolean email) {
        if (checkInput(layout, errorMessage)) {
            String s = Objects.requireNonNull(layout.getEditText()).getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                layout.setError(errorMessage);
                return false;
            }
            return true;
        }

        return false;
    }

    public static void shareText(Context context, String text) {
        context.startActivity(
                Intent.createChooser(
                        new Intent(Intent.ACTION_SEND)
                                .setType("text/plain")
                                .putExtra(Intent.EXTRA_TEXT, text),
                        null
                )
        );
    }

    public static void openBrowser(Context context, String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
        } catch (Exception ignored) {
        }
    }

    public static class LocalDate {

        public static final long SECOND = 1000;
        public static final long MINUTE = 60 * SECOND;
        public static final long HOUR = 60 * MINUTE;
        public static final long DAY = 24 * HOUR;

        public static String formatDateOnly(String s) {
            if (SettingsActivity.dateOnly) {
                return s.split(" ")[0];
            }

            return s;
        }

        @SuppressLint("SimpleDateFormat")
        public static String getCurrentDate() {
            SimpleDateFormat format;
            format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            return format.format(new Date());
        }

        @SuppressLint("SimpleDateFormat")
        public static int getDifferenceDays(String startDate, String endDate) {
            SimpleDateFormat dateTimeFormat;
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

            try {
                long start = dateTimeFormat.parse(startDate).getTime();
                long end = dateTimeFormat.parse(endDate).getTime();

                if (start > end) {
                    return 0;
                }

                double dif = Math.abs(end - start);
                return (int) (dif / DAY);

            } catch (ParseException e) {
                return 0;
            }
        }

        @SuppressLint("SimpleDateFormat")
        public static int getDifferenceHours(String startDate, String endDate) {
            SimpleDateFormat dateTimeFormat;
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

            try {
                long start = dateTimeFormat.parse(startDate).getTime();
                long end = dateTimeFormat.parse(endDate).getTime();

                if (start > end) {
                    return 0;
                }

                double dif = Math.abs(end - start);
                dif %= DAY;
                return (int) (dif / HOUR);

            } catch (ParseException e) {
                return 0;
            }
        }

        @SuppressLint("SimpleDateFormat")
        public static String getEndDateAddMonths(String startDate, int count) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date date;
            try {
                date = format.parse(startDate);
            } catch (ParseException e) {
                return null;
            }

            assert date != null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, count + 1);
            return getDate(calendar);
        }

        @SuppressLint("SimpleDateFormat")
        public static String getEndDateAddDays(String startDate, int count) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date date;
            try {
                date = format.parse(startDate);
            } catch (ParseException e) {
                return null;
            }

            assert date != null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_YEAR, count);

            return getDate(calendar);
        }

        public static long getTime(String s) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            try {
                return format.parse(s).getTime();
            } catch (ParseException e) {
                return 0;
            }
        }

        public static void getDate(Context context, DateSelection dateSelection) {
            Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                month++;
                String y = String.valueOf(year);
                String m = String.valueOf(month);
                String d = String.valueOf(dayOfMonth);
                if (month < 10) m = "0" + month;
                if (dayOfMonth < 10) d = "0" + dayOfMonth;
                String selectedDate = y + "-" + m + "-" + d;

                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);

                if (SettingsActivity.dateOnly) {
                    String a = "AM";
                    String h = String.valueOf(mHour);
                    String mm = String.valueOf(mMinute);

                    if (mHour == 0) h = "12";
                    else if (mHour < 10) h = "0" + mHour;
                    else if (mHour > 12) h = String.valueOf(Math.abs(12 - mHour));
                    if (mHour >= 12) a = "PM";

                    if (mMinute < 10) mm = "0" + mMinute;
                    String selectedTime = h + ":" + mm + " " + a;
                    dateSelection.onSelected(selectedDate + " " + selectedTime);
                } else {
                    new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                        String h = String.valueOf(hourOfDay);
                        String mm = String.valueOf(minute);
                        String a = "AM";

                        if (hourOfDay == 0) h = "12";
                        else if (hourOfDay < 10) h = "0" + hourOfDay;
                        else if (hourOfDay > 12) h = String.valueOf(Math.abs(12 - hourOfDay));
                        if (hourOfDay >= 12) a = "PM";

                        if (minute < 10) mm = "0" + minute;
                        String selectedTime = h + ":" + mm + " " + a;
                        dateSelection.onSelected(selectedDate + " " + selectedTime);
                    }, mHour, mMinute, false).show();
                }
            }, mYear, mMonth, mDay).show();
        }

        public static String getDate(Calendar calendar) {
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            String y = String.valueOf(mYear);
            String m = String.valueOf(mMonth);
            String d = String.valueOf(mDay);
            if (mMonth < 10) {
                m = "0" + mMonth;
            }
            if (mDay < 10) {
                d = "0" + mDay;
            }
            String selectedDateTime = y + "-" + m + "-" + d;

            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String h = String.valueOf(hourOfDay);
            String mm = String.valueOf(minute);
            String a = "AM";

            if (hourOfDay == 0) h = "12";
            else if (hourOfDay < 10) h = "0" + hourOfDay;
            else if (hourOfDay > 12) h = String.valueOf(Math.abs(12 - hourOfDay));
            if (hourOfDay >= 12) a = "PM";

            if (minute < 10) mm = "0" + minute;
            String selectedTime = h + ":" + mm + " " + a;
            selectedDateTime += (" " + selectedTime);

            return selectedDateTime;
        }

        public interface DateSelection {
            void onSelected(String date);
        }

    }

    public static void sendEmail(Context context, String email) {
        context.startActivity(
                Intent.createChooser(
                        new Intent(Intent.ACTION_VIEW)
                                .setData(
                                        Uri.parse("mailto:?to=" + email)
                                ),
                        context.getString(R.string.send_email)
                )
        );
    }

    public static void sendSMS(Context context, List<String> numbers, String sms) {
        StringBuilder uri = new StringBuilder("smsto:");
        for (String s : numbers) {
            uri.append(s).append(";");
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri.toString()));
        intent.putExtra("sms_body", sms);
        context.startActivity(intent);
    }

    public static void makeCall(Context context, String mobileNumber) {
        context.startActivity(
                Intent.createChooser(
                        new Intent(Intent.ACTION_DIAL)
                                .setData(
                                        Uri.parse("tel:" + mobileNumber)
                                ),
                        context.getString(R.string.make_call)
                )
        );
    }

}
