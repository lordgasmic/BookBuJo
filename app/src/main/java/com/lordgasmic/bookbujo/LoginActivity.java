package com.lordgasmic.bookbujo;

import static com.lordgasmic.bookbujo.Constants.AUTH_HEADER;
import static com.lordgasmic.bookbujo.Constants.LOGIN_REQUEST_CODE;
import static com.lordgasmic.bookbujo.Constants.LOGIN_SERVER;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        txtUsername.setSelection(0);
        txtUsername.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                txtPassword.setSelection(0);
                return true;
            }
            return false;
        });

        txtPassword.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                handleRequest();
                return true;
            }
            return false;
        });

        btnSubmit.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                handleRequest();
                return true;
            }
            return false;
        });

        btnSubmit.setOnClickListener(view -> handleRequest());
    }

    private void handleRequest() {
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        Map<String, String> params = new HashMap<>();
        params.put("username", txtUsername.getText().toString());
        params.put("password", txtPassword.getText().toString());
        JSONObject jsonObject = new JSONObject(params);

        CustomRequest request = new CustomRequest(Request.Method.POST, LOGIN_SERVER, null, jsonObject,
                this::extractAuthToken, error -> Toaster.makeToast(this, "Failed to authenticate"));

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
    }

    private void extractAuthToken(JSONObject jsonObject) {
        try {
            getIntent().putExtra(AUTH_HEADER, jsonObject.getString("authToken"));
            setResult(LOGIN_REQUEST_CODE, getIntent());
            finish();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
