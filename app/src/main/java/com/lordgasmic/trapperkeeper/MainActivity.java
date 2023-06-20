package com.lordgasmic.trapperkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> Casc.initScan(MainActivity.this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result

            String upc = intent.getStringExtra("SCAN_RESULT");
            String supl = intent.getStringExtra("SCAN_RESULT_UPC_EAN_EXTENSION");

            if (supl == null || supl.length() != 5) {
                Toast toast = Toast.makeText(this, "UPC did not fully scan", Toast.LENGTH_LONG);
                toast.show();
                Casc.initScan(this);
            } else {


                // todo: clean
                TextView txtView = findViewById(R.id.txtContent);
                txtView.setText("result: " + upc + "; extension: " + supl);


                Map<String, String> map = new HashMap<>();
                map.put("upc", upc);
                map.put("supl", supl);

                Request request = new CustomRequest(Request.Method.POST,
                                                    "http://73.144.144.163:8081/comic/parse",
                                                    map,
                                                    response -> {
                                                        Intent responseIntent = new Intent(this, ResponseActivity.class);

                                                        try {
                                                            responseIntent.putExtra("title", response.getString("title"));
                                                            responseIntent.putExtra("imageUrl", response.getString("imageUrl"));
                                                            responseIntent.putExtra("issue", response.getString("issue"));
                                                            responseIntent.putExtra("volume", response.getString("volume"));
                                                            responseIntent.putExtra("variant", response.getBoolean("variant"));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(responseIntent);
                                                    },
                                                    error -> {
                                                        Intent derp = new Intent(this, MainActivity.class);
                                                        System.out.println(error.getMessage());
                                                        // derp.putExtra("error", error);
                                                        // startActivity(derp);
                                                    });
                request.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });
                MyVolley.getInstance(this).addToRequestQueue(request);

                // todo: new intent spinner while waiting on response
                Intent spinner = new Intent(this, SpinnerActivity.class);
                startActivity(spinner);
            }
        }
    }
}
