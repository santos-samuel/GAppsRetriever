package com.example.mvpexample.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class AppListener extends BroadcastReceiver {

    private AppListenerService appListenerService;

    public AppListener(AppListenerService appListenerService) {
        this.appListenerService = appListenerService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("INSTALL", "An app has been installed!");

        Uri installedApkUri = intent.getData();
        String installedPackageName = installedApkUri.getEncodedSchemeSpecificPart();
        this.appListenerService.notifyAppInstalled(installedPackageName);
    }
}
