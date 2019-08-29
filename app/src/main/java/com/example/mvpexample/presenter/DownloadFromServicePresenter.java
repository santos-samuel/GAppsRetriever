package com.example.mvpexample.presenter;

import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.DownloadFromServiceFragment;
import com.example.mvpexample.view.SelectAndInspectApkFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class DownloadFromServicePresenter {
    private IDownloadFromServiceView view;
    private RequestManager requestManager;
    private FragmentNavigator fragNavigator;


    public DownloadFromServicePresenter(IDownloadFromServiceView view, RequestManager requestManager, FragmentNavigator fragNavigator) {
        this.view = view;
        this.requestManager = requestManager;
        this.fragNavigator = fragNavigator;
    }

    public void processInstalledApp(String installedPackageName) {
        Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals(Constants.GOOGLE_GPS_PACKAGE_NAME)) {
            //delete apk file?
            view.finishActivity();
        }

        else { // A new app has been installed
            try {
                boolean requiresGPS = requestManager.doesThisAppRequireGooglePlayServices(installedPackageName);

                if (requiresGPS) {
                    int resultCode = requestManager.checkIfGooglePlayServicesIsAvailable();
                    if (resultCode != ConnectionResult.SUCCESS) {
                        Log.d("CONNECTION RESULT", "" + resultCode);
                        switch (resultCode) {
                            case ConnectionResult.SERVICE_MISSING:                  // 1
                            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:  // 2
                                view.showAskUserIfHeWantsToDownloadDialog();
                                break;

                            case ConnectionResult.SERVICE_INVALID:                  // 9
                                view.showDeviceNotSupportedDialog();
                                break;

                            case ConnectionResult.SERVICE_DISABLED:                 // 3
                                view.showGooglePlayServicesIsDisabledDialog();
                                break;

                            case ConnectionResult.SERVICE_UPDATING:                 // 18
                                // no problem?
                                break;

                            default:
                                // no other possible ConnectionResults
                                break;
                        }
                    }
                    else {                                                          // 0
                        view.showOkDialog();
                    }
                }

                else {
                    view.showToastMessage("The installed/changed App doesn't need GPS");
                    view.finishActivity();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeFragment(Fragment newFrag) {
        fragNavigator.navigateTo(newFrag, false);
    }
}
