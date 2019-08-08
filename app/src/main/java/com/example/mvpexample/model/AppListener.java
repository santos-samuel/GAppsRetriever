package com.example.mvpexample.model;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.example.mvpexample.view.MainActivity;

public class AppListener extends BroadcastReceiver {

    private final RequestManager requestManager = new RequestManager(null, null, null, null);
    private final Activity mainActivity = new MainActivity();
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
