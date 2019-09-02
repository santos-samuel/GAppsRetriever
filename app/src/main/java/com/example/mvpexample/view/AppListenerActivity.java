package com.example.mvpexample.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mvpexample.R;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;

public class AppListenerActivity extends MainActivity {

    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_listener);


        this.requestManager = new RequestManager(null, getPackageManager(), getContentResolver(), this);
        this.fragNavigator = new FragmentNavigator(this); // init navigator with main activity
        DownloadFromServiceFragment fragment = new DownloadFromServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString("installedPackageName", getIntent().getExtras().getString("installedPackageName"));
        fragment.setArguments(bundle);

        this.fragNavigator.navigateTo(fragment, true);


        /*super.onCreate(savedInstanceState);

        String installedPackageName = getIntent().getExtras().getString("installedPackageName");

        this.requestManager = new RequestManager(null, getPackageManager(), getContentResolver(), this);

        Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals(Constants.GOOGLE_PLAY_SERVICES_PACKAGE_NAME)) {
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
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // requestCode == 1 means the result for package-installer activity
        if (requestCode == 51)
        {
            // resultCode == RESULT_CANCELED means user pressed `Done` button
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "User pressed 'Done' button", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_OK) {
                // resultCode == RESULT_OK means user pressed `Open` button
                Toast.makeText(this, "User pressed 'Open' button", Toast.LENGTH_SHORT).show();
            }
        }
    }
}