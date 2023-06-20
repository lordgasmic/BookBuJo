package com.lordgasmic.trapperkeeper;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

public class Casc {

    public static void initScan(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.addExtra("ALLOWED_EAN_EXTENSIONS", new int[] {5});
        integrator.initiateScan();
    }
}
