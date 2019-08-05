package com.example.mvpexample.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.mvpexample.R;
import com.example.mvpexample.model.AppListener;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.PersistentMemory;
import com.example.mvpexample.model.RequestManager;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;
    private PersistentMemory persistentMemory;
    private BroadcastReceiver appListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askForReadExternalStoragePermission();
        askForWriteExternalStoragePermission();

        init();
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("START", "onStart()");
        this.appListener = new AppListener(requestManager, this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        registerReceiver(appListener, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(appListener);
    }

    private void askForWriteExternalStoragePermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void askForReadExternalStoragePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void init() {
        this.persistentMemory = new PersistentMemory();
        this.requestManager = new RequestManager(persistentMemory, getPackageManager(), getContentResolver(), this);
        this.fragNavigator = new FragmentNavigator(this); // init navigator with main activity
        this.fragNavigator.navigateTo(new ColorFragment(), true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // move on
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.exit(1);
                }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public FragmentNavigator getFragNavigator() {
        return fragNavigator;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
