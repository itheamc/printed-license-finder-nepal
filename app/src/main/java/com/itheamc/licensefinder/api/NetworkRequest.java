package com.itheamc.licensefinder.api;

import android.os.Handler;

import com.itheamc.licensefinder.models.Query;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkRequest {
    private static final String TAG = "NetworkRequest";
    private static NetworkRequest instance;
    private final NetworkCallback callback;
    private final OkHttpClient okHttpClient;
    private final ExecutorService executorService;
    private final Handler handler;
    public final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    // Constructor

    private NetworkRequest(NetworkCallback callback, ExecutorService executorService, Handler handler) {
        this.okHttpClient = new OkHttpClient.Builder().build();
        this.callback = callback;
        this.executorService = executorService;
        this.handler = handler;
    }


    // Getter for Instance
    public static NetworkRequest getInstance(NetworkCallback callback, ExecutorService executorService, Handler handler) {
        if (instance == null) {
            instance = new NetworkRequest(callback, executorService, handler);
        }

        return instance;
    }


    // Function to fetch license
    public void fetchLicense(Query query) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                final String url = "https://www.dotm.gov.np/CheckLicense/ShowDetails";
                String data = "{ \n" +
                        "    \"name\": \"" + query.getName() + "\", \n" +
                        "    \"DlNo\": \"" + query.getDlNo() + "\" \n" +
                        "}";
                RequestBody requestBody = RequestBody.create(data, JSON);
                Request request = new Request.Builder().url(url).post(requestBody).build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        notifyErrors(callback, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject("{licenses:" + Objects.requireNonNull(response.body()).string() + "}");
                            JSONArray jsonArray = jsonObject.getJSONArray("licenses");
                            notifySuccess(callback, jsonArray);
//                    NotifyUtils.logDebug(TAG, jsonArray.toString());
                        } catch (JSONException e) {
                            notifyErrors(callback, e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }


    private void notifySuccess(NetworkCallback c, JSONArray jsonArray) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                c.onSuccess(jsonArray);
            }
        });
    }

    private void notifyErrors(NetworkCallback c, String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                c.onFailure(error);
            }
        });
    }


}
