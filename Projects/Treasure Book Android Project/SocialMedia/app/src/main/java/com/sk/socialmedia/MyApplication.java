package com.sk.socialmedia;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate();
    }
}
