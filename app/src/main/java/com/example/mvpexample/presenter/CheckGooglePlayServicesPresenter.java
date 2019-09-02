package com.example.mvpexample.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.google.android.gms.common.ConnectionResult;

public class CheckGooglePlayServicesPresenter {
    private final ICheckGooglePlayServicesView view;
    private final FragmentNavigator fragmentNavigator;
    private RequestManager requestManager;

    public CheckGooglePlayServicesPresenter(ICheckGooglePlayServicesView view, RequestManager requestManager, FragmentNavigator fragNavigator, Bundle arguments) {
        this.requestManager = requestManager;
        this.view = view;
        this.fragmentNavigator = fragNavigator;

        initFragment(arguments);
    }

    private void initFragment(Bundle arguments) {
        view.showIfSelectedAppNeedsGooglePlayServices((String) arguments.get("inspectResult"));
    }

    public void checkGPSAvailability() {
        int resultCode = requestManager.checkIfGooglePlayServicesIsAvailable();
        switch (resultCode) {
            case ConnectionResult.SERVICE_MISSING:                  // 1
                view.showToastMessage("SERVICE_MISSING");
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:  // 2
                view.showToastMessage("SERVICE_VERSION_UPDATE_REQUIRED");
                break;

            case ConnectionResult.SERVICE_INVALID:                  // 9
                view.showToastMessage("SERVICE_INVALID");
                break;

            case ConnectionResult.SERVICE_DISABLED:                 // 3
                view.showToastMessage("SERVICE_DISABLED");
                break;

            case ConnectionResult.SERVICE_UPDATING:                 // 18
                view.showToastMessage("SERVICE_UPDATING");
                break;
            case ConnectionResult.SUCCESS:                          // 0
                view.showToastMessage("SUCCESS");
                break;
            default:
                // no other possible ConnectionResults
                break;
        }
    }

    public void changeFragment(Fragment newFrag) {
        fragmentNavigator.navigateTo(newFrag, false);
    }
}
