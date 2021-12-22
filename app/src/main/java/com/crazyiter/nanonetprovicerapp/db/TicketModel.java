package com.crazyiter.nanonetprovicerapp.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicketModel {

    private final String id;
    private final String customerId;
    private final String providerId;
    private final String body;
    private final String dateTime;
    private final ArrayList<TicketAnswerModel> answerModels;
    private boolean isActive;

    public TicketModel(String id, String customerId, String providerId, String body, String dateTime, boolean isActive) {
        this.id = id;
        this.customerId = customerId;
        this.providerId = providerId;
        this.body = body;
        this.dateTime = dateTime;
        this.answerModels = new ArrayList<>();
        this.isActive = isActive;
    }

    public void addAnswers(JSONArray jsonArray) {
        answerModels.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                answerModels.add(TicketAnswerModel.fromJson(jsonArray.getJSONObject(i)));
            } catch (JSONException ignored) {
            }
        }
    }

    public void addAnswer(TicketAnswerModel answerModel) {
        this.answerModels.add(answerModel);
    }

    public void deleteAnswer(TicketAnswerModel answerModel) {
        this.answerModels.remove(answerModel);
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("customerId", customerId);
        map.put("providerId", providerId);
        map.put("body", body);
        map.put("dateTime", dateTime);
        JSONArray answers = new JSONArray();
        for (TicketAnswerModel answerModel : answerModels) {
            answers.put(answerModel.toJson());
        }
        map.put("answers", answers.toString());
        return map;
    }

    public JSONObject toJson() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            object.put("customerId", customerId);
            object.put("providerId", providerId);
            object.put("body", body);
            object.put("dateTime", dateTime);

            JSONArray answers = new JSONArray();
            for (TicketAnswerModel answerModel : answerModels) {
                answers.put(answerModel.toJson());
            }
        } catch (JSONException e) {
            return null;
        }

        return object;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getBody() {
        return body;
    }

    public String getDateTime() {
        return dateTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ArrayList<TicketAnswerModel> getAnswerModels() {
        return answerModels;
    }

    public static class TicketAnswerModel {
        private final String id;
        private final String senderId;
        private final String body;
        private final String dateTime;

        public TicketAnswerModel(String id, String senderId, String body, String dateTime) {
            this.id = id;
            this.senderId = senderId;
            this.body = body;
            this.dateTime = dateTime;
        }

        public static TicketAnswerModel fromJson(JSONObject jsonObject) {
            try {
                return new TicketAnswerModel(
                        jsonObject.getString("id"),
                        jsonObject.getString("senderId"),
                        jsonObject.getString("body"),
                        jsonObject.getString("dateTime")
                );
            } catch (JSONException e) {
                return null;
            }
        }

        public JSONObject toJson() {
            JSONObject object = new JSONObject();
            try {
                object.put("id", id);
                object.put("senderId", senderId);
                object.put("body", body);
                object.put("dateTime", dateTime);
            } catch (JSONException e) {
                return null;
            }

            return object;
        }

        public Map<String, Object> getMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("senderId", senderId);
            map.put("body", body);
            map.put("dateTime", dateTime);
            return map;
        }

        public String getId() {
            return id;
        }

        public String getSenderId() {
            return senderId;
        }

        public String getBody() {
            return body;
        }

        public String getDateTime() {
            return dateTime;
        }
    }

}
