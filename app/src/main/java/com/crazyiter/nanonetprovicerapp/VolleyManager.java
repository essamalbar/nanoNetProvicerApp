package com.crazyiter.nanonetprovicerapp;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crazyiter.nanonetprovicerapp.db.AlertModel;
import com.crazyiter.nanonetprovicerapp.db.Customer;
import com.crazyiter.nanonetprovicerapp.db.CustomerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class VolleyManager {

    public static RequestQueue REQUEST_QUEUE;

    private final static String sendNotificationUrl = "https://fcm.googleapis.com/fcm/send";
    private final static String KEY = "AAAARCkQjmQ:APA91bEDUOAm4MzHqE6cUaJng32QRFgrKxeg8Jxwyk0EUmIqYEcFDSyFtvRWefMXSeECHFZhrxVdpdxZ-bfqHbvOVvIyE9lP5jCWwEFvM83fnQSq-rAZaXNo_mrSKHfctLxj52a_56TE";

    public static void sendNotification(String token, String title, String msg, String ticketId) {
        JSONArray fcm = new JSONArray();
        fcm.put(token);
        JSONObject body = new JSONObject();
        try {
            body.put("registration_ids", fcm);
            JSONObject notification = new JSONObject();
            notification.put("body", msg);
            notification.put("title", title);
            notification.put("ticketId", ticketId);
            body.put("data", notification);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(POST, sendNotificationUrl, body, response -> Log.e("notification", response.toString()), error -> Log.e("notification", error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + KEY);
                return map;
            }
        };
        REQUEST_QUEUE.add(request);
    }

    public static void sendNotification(AlertModel alertModel) throws JSONException {
        JSONArray fcm = new JSONArray();
        for (int i = 0; i < alertModel.getCustomerIds().length(); i++) {
            for (int j = 0; j < CustomerManager.customers.size(); j++) {
                Customer c = CustomerManager.customers.get(j);
                if (c.getId() == alertModel.getCustomerIds().get(i)) {
                    fcm.put(c.getFcm());
                    break;
                }
            }
        }

        JSONObject body = new JSONObject();
        body.put("registration_ids", fcm);
        JSONObject notification = new JSONObject();
        notification.put("body", alertModel.getMessage());
        body.put("notification", notification);

        JsonObjectRequest request = new JsonObjectRequest(POST, sendNotificationUrl, body, response -> Log.e("notification", response.toString()), error -> Log.e("notification", error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + KEY);
                return map;
            }
        };
        REQUEST_QUEUE.add(request);
    }

}
