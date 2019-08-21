package com.example.mvpexample.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.mvpexample.R;
import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;

public class AppListenerActivity extends MainActivity {

    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*super.onCreate(savedInstanceState);

        this.fragNavigator = new FragmentNavigator(this); // init navigator with main activity

        this.requestManager = new RequestManager(null, getPackageManager(), getContentResolver(), this);

        setContentView(R.layout.activity_app_listener);
        DownloadFragment fragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putString("installedPackageName", getIntent().getExtras().getString("installedPackageName"));
        fragment.setArguments(bundle);
        this.fragNavigator.navigateTo(fragment, true);

*/

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