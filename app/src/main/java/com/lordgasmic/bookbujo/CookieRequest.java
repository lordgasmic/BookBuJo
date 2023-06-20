package com.lordgasmic.bookbujo;

import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class CookieRequest extends JsonObjectRequest {

    private final Response.Listener<JSONObject> responseListener;
    private String rawCookies;

    public CookieRequest(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> responseListener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonObject, responseListener, errorListener);

        this.responseListener = responseListener;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        Map<String, String> responseHeaders = response.headers;
        rawCookies = responseHeaders.get("Set-Cookie");
        Log.i("cookies", rawCookies);
        return super.parseNetworkResponse(response);
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        responseListener.onResponse(response);
    }

    public String getRawCookies() {
        return rawCookies;
    }
}
