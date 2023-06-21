package com.lordgasmic.bookbujo;

import static com.lordgasmic.bookbujo.Constants.AUTH_HEADER;
import static com.lordgasmic.bookbujo.Constants.BOOKBUJO_ENDPOINT;
import static com.lordgasmic.bookbujo.Constants.LOGIN_REQUEST_CODE;
import static com.lordgasmic.bookbujo.Toaster.makeToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private String authHeader = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (authHeader.isBlank()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
        }

        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btn_process);

        GmsBarcodeScannerOptions options = new GmsBarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_UPC_A)
                .build();
        GmsBarcodeScanner scanner = GmsBarcodeScanning.getClient(this, options);

        btn.setOnClickListener(view -> scanner.startScan()
                .addOnSuccessListener(this::onBarcodeSuccess)
                .addOnFailureListener(failure -> makeToast(this, failure.getMessage())));
    }

    private void onBarcodeSuccess(Barcode barcode) {
        TextView view = findViewById(R.id.txt_isbn);
        view.setText(barcode.getRawValue());
        TextView txtVolley = findViewById(R.id.txt_volley);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authHeader);
        String url = BOOKBUJO_ENDPOINT + "/" + barcode.getRawValue();
        Log.i("url", url);
        JsonObjectRequest request = new CustomRequest(Request.Method.GET, url, headers, null, this::extractDataFromISBNLookup, error -> txtVolley.setText(error.getMessage()));
        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
    }

    private void extractDataFromISBNLookup(JSONObject jsonObject) {
        TextView txtVolley = findViewById(R.id.txt_volley);
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            JSONObject item = items.getJSONObject(0);
            JSONObject volumeInfo = item.getJSONObject("volumeInfo");
            String title = volumeInfo.getString("title");
            txtVolley.setText(title);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == LOGIN_REQUEST_CODE) {
            authHeader = intent.getStringExtra(AUTH_HEADER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }
}
