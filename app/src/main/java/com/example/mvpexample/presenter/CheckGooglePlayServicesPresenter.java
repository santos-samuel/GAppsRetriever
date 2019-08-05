package com.example.mvpexample.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.example.mvpexample.model.DeviceNotSupportedException;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;

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
        try {
            if (requestManager.checkIfGooglePlayServicesIsAvailable()) {
                view.showDialog("Everything is ok!");
            } else {
                view.showDialog("Google play services is not installed/updated/valid!");
            }
        } catch (DeviceNotSupportedException e) {
            view.showDialog("This device is not supported!");
        }

    }

    public void changeFragment(Fragment newFrag) {
        fragmentNavigator.navigateTo(newFrag, false);
    }
}
