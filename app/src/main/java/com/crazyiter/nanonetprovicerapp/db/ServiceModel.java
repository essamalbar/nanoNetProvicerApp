package com.crazyiter.nanonetprovicerapp.db;

import java.util.HashMap;
import java.util.Map;

public class ServiceModel {
    private final String id;
    private final String name;
    private final String url;
    private final String image;
    private final String providerId;
    private final String date;

    public ServiceModel(String id, String name, String url, String image, String providerId, String date) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
        this.providerId = providerId;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("url", url);
        map.put("image", image);
        map.put("providerId", providerId);
        map.put("date", date);
        return map;
    }
}
