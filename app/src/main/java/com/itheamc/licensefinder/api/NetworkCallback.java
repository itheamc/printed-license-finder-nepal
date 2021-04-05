package com.itheamc.licensefinder.api;

import org.json.JSONArray;

public interface NetworkCallback {
    void onSuccess(JSONArray jsonArray);
    void onFailure(String error);
}
