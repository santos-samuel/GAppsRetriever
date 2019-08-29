package com.example.mvpexample.view;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.mvpexample.R;
import com.example.mvpexample.model.AppListenerService;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.PersistentMemory;
import com.example.mvpexample.model.RequestManager;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_INSTALL_PACKAGES = 3;

    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;
    private PersistentMemory persistentMemory;
    Intent appListenerIntent;
    private AppListenerService appListenerService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

       appListenerService = new AppListenerService();
        appListenerIntent = new Intent(this, appListenerService.getClass());
        if (!isMyServiceRunning(appListenerService.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(new Intent(this, AppListenerService.class));
            } else {
                this.startService(new Intent(this, AppListenerService.class));
            }
        }

        askForReadExternalStoragePermission();
        askForWriteExternalStoragePermission();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), MY_PERMISSIONS_REQUEST_INSTALL_PACKAGES);
            } else {
                //callInstallProcess();
            }
        } else {
            //callInstallProcess();
        }*/

        //askForInstallPackagesPermission();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(appListenerIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
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
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void askForInstallPackagesPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.REQUEST_INSTALL_PACKAGES)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},
                        MY_PERMISSIONS_REQUEST_INSTALL_PACKAGES);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSIONS_REQUEST_INSTALL_PACKAGES && resultCode == Activity.RESULT_OK) {
            if (getPackageManager().canRequestPackageInstalls()) {
                //callInstallProcess();
            }
        } else {
            //give the error
        }
    }

    public FragmentNavigator getFragNavigator() {
        return fragNavigator;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
