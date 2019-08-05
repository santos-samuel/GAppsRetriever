package com.example.mvpexample.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.List;

public class AppListener extends BroadcastReceiver {

    private final RequestManager requestManager;
    private final PackageManager packageManager;

    public AppListener(RequestManager requestManager, PackageManager packageManager) {
        this.requestManager = requestManager;
        this.packageManager = packageManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("INSTALL", "An app has been installed!");

        // Delete installed apk file
        Uri installedApkUri = intent.getData();
        String installedPackageName = installedApkUri.getEncodedSchemeSpecificPart();

        Log.d("INSTALLED APP", installedPackageName);
        // assuming that it was this code that programatically downloaded and installed the GPS apk
        if (installedPackageName.equals("com.google.android.gms"))
            requestManager.deleteApkFileOnStorage();

        else { // A new app has been installed
            try {
                requestManager.doesThisAppRequireGooglePlayServices(installedPackageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
