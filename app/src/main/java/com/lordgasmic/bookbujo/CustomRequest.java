package com.lordgasmic.bookbujo;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.Map;

public class CustomRequest extends JsonObjectRequest {

    private final Response.Listener<JSONObject> responseListener;
    private Map<String, String> headers;

    public CustomRequest(int method, String url, Map<String, String> headers, JSONObject jsonObject, Response.Listener<JSONObject> responseListener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonObject, responseListener, errorListener);

        this.responseListener = responseListener;
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers == null) {
            headers = super.getHeaders();
        } else {
            headers.putAll(super.getHeaders());
        }
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        responseListener.onResponse(response);
    }
}
