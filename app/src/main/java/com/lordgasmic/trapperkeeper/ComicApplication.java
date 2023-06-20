package com.lordgasmic.trapperkeeper;

import android.app.Application;

public class ComicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DbHelper.createInstance(getApplicationContext(), getResources());
    }
}