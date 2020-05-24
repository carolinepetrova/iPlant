package com.project.iplant.scheduler_service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private String date;

    private List<String> plantIDs = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getPlantIDs() {
        return plantIDs;
    }

    public void setPlantIDs(List<String> plantIDs) {
        this.plantIDs = plantIDs;
    }

    public String toJsonObject() {
        JsonObject data = new JsonObject();
        data.addProperty("date", this.date);
        JsonArray plantIDs = new JsonArray();
        for(String id : this.plantIDs) {
            plantIDs.add(id);
        }
        data.add("plantIDs", plantIDs);
        return data.toString();
    }
}
