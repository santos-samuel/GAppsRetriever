package com.example.mvpexample.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AppListener extends BroadcastReceiver {

    private final RequestManager requestManager;

    public AppListener(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("INSTALL", "An app has been installed!");

        Uri installedApkUri = intent.getData();

        String packageName = installedApkUri.getEncodedSchemeSpecificPart();
        // assuming that it was this code that programatically downloaded and installed the GPS apk
        if (packageName.equals("com.google.android.gms"))
            requestManager.deleteApkFileOnStorage();
    }
}
