package com.example.mvpexample.model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.example.mvpexample.view.MainActivity;

public class AppListenerService extends Service {
    private RequestManager requestManager;
    private MainActivity mainActivity;
    private BroadcastReceiver appListener;

    public AppListenerService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startListening();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        stopListening();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartAppListenerService");
        broadcastIntent.setClass(this, AppListenerRestarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    private void stopListening() {
        unregisterReceiver(appListener);
    }

    public void startListening() {
        this.appListener = new AppListener(requestManager, mainActivity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        registerReceiver(appListener, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
