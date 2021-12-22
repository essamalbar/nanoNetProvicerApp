package com.crazyiter.nanonetprovicerapp.db;

import com.crazyiter.nanonetprovicerapp.R;
import com.crazyiter.nanonetprovicerapp.Statics;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Customer implements Serializable {
    private String id;
    private String name;
    private String user;
    private String mobile;
    private String startDate;
    private int cost;
    private int days;
    private String providerId;
    private int amount;

    private String nanoUrl;
    private String nanoUsername;
    private String nanoPassword;

    private String note;

    private String email;
    private String password;
    private JSONArray logs;
    private String fcm = "";

    private boolean isActive;

    public Customer(String id, String name, String user, String mobile, String startDate, int cost, String providerId, int amount, String nanoUrl, String nanoUsername, String nanoPassword, String note, String email, String password, int days, String fcm) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.mobile = mobile;
        this.startDate = startDate;
        this.cost = cost;
        this.providerId = providerId;
        this.amount = amount;
        this.nanoUrl = nanoUrl;
        this.nanoUsername = nanoUsername;
        this.nanoPassword = nanoPassword;
        this.note = note;
        this.email = email;
        this.password = password;
        this.days = days;
        this.fcm = fcm;
    }

    public String getFcm() {
        if (fcm == null) {
            fcm = "";
        }
        return fcm;
    }

    public JSONArray getLogs() {
        if (logs == null) {
            logs = new JSONArray();
        }
        return logs;
    }

    public void addLog(LogModel logModel) {
        if (logModel == null) {
            return;
        }

        if (this.logs == null) {
            this.logs = new JSONArray();
        }
        this.logs.put(logModel.getJson());
    }

    public void setLogs(JSONArray logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "اسم المشترك: " + name + "\n" +
                "اليوزر: " + user + "\n" +
                "سعر الاشتراك الحالي: " + Statics.formatNumber(getCost() * 1000) + "\n" +
                "تاريخ بدء الاشتراك: " + Statics.LocalDate.formatDateOnly(getStartDate()) + "\n" +
                "تاريخ انتهاء الاشتراك: " + Statics.LocalDate.formatDateOnly(getEndDate()) + "\n" +
                "عدد اﻷيام المتبقية: " + getDiffDays() + "\n" +
                "الديون: " + Statics.formatNumber(getAmount() * 1000);
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("user", user);
        map.put("email", email);
        map.put("password", password);
        map.put("mobile", mobile);
        map.put("startDate", startDate);
        map.put("days", days);
        map.put("cost", cost);
        map.put("providerId", providerId);
        map.put("amount", amount);

        map.put("nanoUrl", nanoUrl);
        map.put("nanoUsername", nanoUsername);
        map.put("nanoPassword", nanoPassword);

        map.put("note", note);
        map.put("logs", getLogs().toString());

        return map;
    }

    public boolean isNotLinked() {
        return user.isEmpty() || password.isEmpty();
    }

    public String getEndDate() {
        return Statics.LocalDate.getEndDateAddDays(startDate, days);
    }

    public int getDiffDays() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceDays(d, d2);
    }

    public int getRemainsHours() {
        String d = Statics.LocalDate.getCurrentDate();
        String d2 = getEndDate();

        return Statics.LocalDate.getDifferenceHours(d, d2);
    }

    public int getDiffDaysColor() {
        int dif = getDiffDays();
        int difHours = getRemainsHours();

        if (dif > 3) {
            return R.color.colorGreen;
        }

        if (dif > 0 || (dif == 0 && difHours > 0)) {
            return R.color.colorYellow;
        }

        return R.color.colorRed;
    }

    public boolean search(String s) {
        return name.contains(s);
    }

    public boolean isEquals(Customer c) {
        if (email.isEmpty()) {
            return name.equals(c.getName());
        }

        return name.equals(c.getName()) || email.equals(c.getEmail());
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getMobile() {
        return mobile;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    public String getNanoUrl() {
        return nanoUrl;
    }

    public String getNanoUsername() {
        return nanoUsername;
    }

    public String getNanoPassword() {
        return nanoPassword;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setNanoUrl(String nanoUrl) {
        this.nanoUrl = nanoUrl;
    }

    public void setNanoUsername(String nanoUsername) {
        this.nanoUsername = nanoUsername;
    }

    public void setNanoPassword(String nanoPassword) {
        this.nanoPassword = nanoPassword;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}