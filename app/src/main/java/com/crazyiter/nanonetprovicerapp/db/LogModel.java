package com.crazyiter.nanonetprovicerapp.db;

import org.json.JSONException;
import org.json.JSONObject;

public class LogModel {

    private final String id;
    private final String dateTime;
    private final String body;
    private final String customerId;

    public LogModel(String id, String dateTime, String body, String customerId) {
        this.id = id;
        this.dateTime = dateTime;
        this.body = body;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getBody() {
        return body;
    }

    public String getCustomerId() {
        return customerId;
    }

    public JSONObject getJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("dateTime", dateTime);
            object.put("body", body);
            object.put("customerId", customerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
