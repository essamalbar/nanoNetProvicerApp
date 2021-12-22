package com.crazyiter.nanonetprovicerapp.db;

import java.util.HashMap;
import java.util.Map;

public class PlanModel {
    private final String id;
    private final String name;
    private final int price;
    private final String description;
    private final String providerId;

    public PlanModel(String id, String name, int price, String description, String providerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.providerId = providerId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getProviderId() {
        return providerId;
    }

    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("description", description);
        map.put("price", price);
        map.put("providerId", providerId);
        return map;
    }
}
