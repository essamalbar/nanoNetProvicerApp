package com.crazyiter.nanonetprovicerapp.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.crazyiter.nanonetprovicerapp.FirebaseCloudMessagingService;
import com.crazyiter.nanonetprovicerapp.R;
import com.crazyiter.nanonetprovicerapp.Statics;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Provider implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private String startDate;
    private int type;
    private int count;
    private int orderBy;
    private String fcm;
    private String adminId;
    private boolean isOnline;

    private boolean isActive = true;

    private boolean renewAlert = false;
    private boolean payAlert = false;
    private boolean amountAlert = false;
    private boolean ticketAlert = false;

    public Provider(String id, String name, String email, String password, String startDate, int type, int count, int orderBy, String adminId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.startDate = startDate;
        this.type = type;
        this.count = count;
        this.orderBy = orderBy;
        this.adminId = adminId;
    }

    public void saveShared(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("startDate", startDate);
        editor.putInt("type", type);
        editor.putInt("count", count);
        editor.putInt("orderBy", orderBy);
        editor.putString("adminId", adminId);

        editor.putBoolean("renewAlert", renewAlert);
        editor.putBoolean("payAlert", payAlert);
        editor.putBoolean("amountAlert", amountAlert);
        editor.putBoolean("ticketAlert", ticketAlert);

        editor.apply();
    }

    public static Provider getShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        String _id = sharedPreferences.getString("id", null);
        if (_id == null) {
            return null;
        }

        Provider provider = new Provider(
                _id,
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("password", null),
                sharedPreferences.getString("startDate", null),
                sharedPreferences.getInt("type", -1),
                sharedPreferences.getInt("count", -1),
                sharedPreferences.getInt("orderBy", R.id.nameRB),
                sharedPreferences.getString("adminId", null)
        );

        provider.setRenewAlert(sharedPreferences.getBoolean("renewAlert", false));
        provider.setPayAlert(sharedPreferences.getBoolean("payAlert", false));
        provider.setAmountAlert(sharedPreferences.getBoolean("amountAlert", false));
        provider.setTicketAlert(sharedPreferences.getBoolean("ticketAlert", false));

        provider.setFcm(FirebaseCloudMessagingService.getToken(context));
        return provider;
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    @SuppressLint("SimpleDateFormat")
    public String getEndDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
        Date date;

        try {
            date = format.parse(startDate);
        } catch (ParseException e) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);

        if (type == 0) { // day
            calendar.add(Calendar.DAY_OF_MONTH, count);
        }

        if (type == 1) { // month
            calendar.add(Calendar.MONTH, count);
        }

        if (type == 2) { // year
            calendar.add(Calendar.YEAR, count);
        }

        return Statics.LocalDate.getDate(calendar);
    }

    public int getDiffDays() {
        return Statics.LocalDate.getDifferenceDays(Statics.LocalDate.getCurrentDate(), getEndDate());
    }

    public int getRemainsHours() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceHours(d, d2);
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("startDate", startDate);
        map.put("type", type);
        map.put("count", count);
        map.put("fcm", fcm);
        map.put("orderBy", orderBy);
        map.put("isOnline", isOnline);

        map.put("renewAlert", renewAlert);
        map.put("payAlert", payAlert);
        map.put("amountAlert", amountAlert);
        map.put("ticketAlert", ticketAlert);
        return map;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", startDate='" + startDate + '\'' +
                ", type=" + type +
                ", count=" + count +
                ", orderBy=" + orderBy +
                ", fcm='" + fcm + '\'' +
                ", adminId='" + adminId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isRenewAlert() {
        return renewAlert;
    }

    public void setRenewAlert(boolean renewAlert) {
        this.renewAlert = renewAlert;
    }

    public boolean isPayAlert() {
        return payAlert;
    }

    public void setPayAlert(boolean payAlert) {
        this.payAlert = payAlert;
    }

    public boolean isAmountAlert() {
        return amountAlert;
    }

    public boolean isTicketAlert() {
        return ticketAlert;
    }

    public void setTicketAlert(boolean ticketAlert) {
        this.ticketAlert = ticketAlert;
    }

    public void setAmountAlert(boolean amountAlert) {
        this.amountAlert = amountAlert;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}