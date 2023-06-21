package com.lordgasmic.bookbujo;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

    public static void makeToast(Context ctx, String text) {
        Toast toast = Toast.makeText(ctx, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
