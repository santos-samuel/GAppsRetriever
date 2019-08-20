package com.example.mvpexample.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.RequestManager;

public class AppListenerActivity extends AppCompatActivity {

    private RequestManager requestManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String installedPackageName = getIntent().getExtras().getString("installedPackageName");

        this.requestManager = new RequestManager(null, getPackageManager(), getContentResolver(), this);

        Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals(Constants.GOOGLE_GPS_PACKAGE_NAME)) {
            //requestManager.deleteApkFileOnStorage();
        }

        else { // A new app has been installed
            try {
                boolean requiresGPS = requestManager.doesThisAppRequireGooglePlayServices(installedPackageName);

                if (requiresGPS) {
                    requestManager.checkIfGooglePlayServicesIsAvailable();
                }

                else {
                    Toast.makeText(this, "The installed/changed App doesn't need GPS", Toast.LENGTH_SHORT).show();
                    this.finish(); // close activity
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}