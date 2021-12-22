package com.crazyiter.nanonetprovicerapp.db;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class AlertModel {

    private String id;
    private JSONArray customerIds;
    private String providerId;
    private String message;
    private String dateTime;
    private boolean isNotified;

    private final int typeIconRes;
    private final int typeIconColor;

    public AlertModel(String id, String providerId, String message, String dateTime, boolean isNotified, int typeIconRes, int typeIconColor) {
        this.id = id;
        this.providerId = providerId;
        this.message = message;
        this.dateTime = dateTime;
        this.isNotified = isNotified;
        this.typeIconColor = typeIconColor;
        this.typeIconRes = typeIconRes;
        this.customerIds = new JSONArray();
    }

    public void addCustomer(Customer customer) {
        customerIds.put(customer.getId());
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("providerId", providerId);
        map.put("message", message);
        map.put("dateTime", dateTime);
        map.put("isNotified", isNotified);
        map.put("customerIds", customerIds.toString());
        map.put("typeRes", typeIconRes);
        map.put("typeColor", typeIconColor);
        return map;
    }

    public JSONArray getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(JSONArray customerIds) {
        this.customerIds = customerIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public int getTypeIconRes() {
        return typeIconRes;
    }

    public int getTypeIconColor() {
        return typeIconColor;
    }
}
