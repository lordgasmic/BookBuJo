package com.lordgasmic.bookbujo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private CookieRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .addOnFailureListener(failure -> makeToast(failure.getMessage())));

        TextView txtVolley = findViewById(R.id.txt_volley);

        String url = getString(R.string.login_server);

        Map<String, String> params = new HashMap<>();
        params.put("username", "lordgasmic");
        params.put("password", "v1rtual!");
        JSONObject jsonObject = new JSONObject(params);

        request = new CookieRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        txtVolley.setText("Response is: " + response.getString("roles"));
                        extractCookie();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> txtVolley.setText("That didn't work!"));

        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);

    }

    private void extractCookie() {
        TextView view = findViewById(R.id.txt_cookie);

//        if (request == null) {
//            view.setText("null request");
//            return;
//        }
//        try {
////            Optional<String> headers = request.getHeaders().keySet().stream().reduce(String::concat);
//            StringBuilder sb = new StringBuilder();
//            for (String s : request.getHeaders().keySet()) {
//                sb.append(s).append(" ");
//            }
//            view.setText(request.getHeaders().size() + "");
//        } catch (AuthFailureError e) {
//            view.setText("cookies derped");
//        }
        view.setText(request.getRawCookies());
    }

    private void onBarcodeSuccess(Barcode barcode) {
        TextView view = findViewById(R.id.txt_isbn);
        view.setText(barcode.getRawValue());
        TextView txtVolley = findViewById(R.id.txt_volley);

        String url = getString(R.string.bookbujo) + "/" + barcode.getRawValue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                view.setText(response.getString("title"));
            } catch (JSONException e) {
                view.setText("there no fish");
            }
        }, error -> {
            txtVolley.setText(error.getMessage());
        });
        RequestQueueSingleton.getInstance(this).getRequestQueue().add(request);
    }

    private void makeToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        makeToast("found on result");
//        intent.
        Log.i("info", "derp");
        intent.getExtras().keySet().forEach(System.out::println);
//        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

//        if (scanResult != null) {
//            // handle scan result
//
//            String upc = intent.getStringExtra("SCAN_RESULT");
//            String supl = intent.getStringExtra("SCAN_RESULT_UPC_EAN_EXTENSION");
//
//            if (supl == null || supl.length() != 5) {
//                Toast toast = Toast.makeText(this, "UPC did not fully scan", Toast.LENGTH_LONG);
//                toast.show();
//                Casc.initScan(this);
//            } else {
//
//
//                // todo: clean
//                TextView txtView = findViewById(R.id.txtContent);
//                txtView.setText("result: " + upc + "; extension: " + supl);
//
//
//                Map<String, String> map = new HashMap<>();
//                map.put("upc", upc);
//                map.put("supl", supl);
//
//                Request request = new CustomRequest(Request.Method.POST,
//                        "http://73.144.144.163:8081/comic/parse",
//                        map,
//                        response -> {
//                            Intent responseIntent = new Intent(this, ResponseActivity.class);
//
//                            try {
//                                responseIntent.putExtra("title", response.getString("title"));
//                                responseIntent.putExtra("imageUrl", response.getString("imageUrl"));
//                                responseIntent.putExtra("issue", response.getString("issue"));
//                                responseIntent.putExtra("volume", response.getString("volume"));
//                                responseIntent.putExtra("variant", response.getBoolean("variant"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            startActivity(responseIntent);
//                        },
//                        error -> {
//                            Intent derp = new Intent(this, MainActivity.class);
//                            System.out.println(error.getMessage());
//                            // derp.putExtra("error", error);
//                            // startActivity(derp);
//                        });
//                request.setRetryPolicy(new RetryPolicy() {
//                    @Override
//                    public int getCurrentTimeout() {
//                        return 50000;
//                    }
//
//                    @Override
//                    public int getCurrentRetryCount() {
//                        return 50000;
//                    }
//
//                    @Override
//                    public void retry(VolleyError error) throws VolleyError {
//
//                    }
//                });
//                MyVolley.getInstance(this).addToRequestQueue(request);
//
//                // todo: new intent spinner while waiting on response
//                Intent spinner = new Intent(this, SpinnerActivity.class);
//                startActivity(spinner);
//            }
//        }
    }
}
