package com.veyndan.generic;

import android.app.Application;

import com.firebase.client.Firebase;

public class Generic extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
