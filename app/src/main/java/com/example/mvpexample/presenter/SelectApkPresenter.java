package com.example.mvpexample.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.SelectApkFragment;

public class SelectApkPresenter {

    private ISelectApkView view;
    private RequestManager requestManager;
    private Uri lastSelectedFileURI;
    FragmentNavigator fragNavigator;
    private String infoFromInspectApk;

    public SelectApkPresenter(ISelectApkView view, RequestManager requestManager, FragmentNavigator fragNavigator) {
        this.view = view;
        this.requestManager = requestManager;
        this.fragNavigator = fragNavigator;
    }

    public void notifySelectedFile(Intent data) {
        lastSelectedFileURI = data.getData();
        view.updateSelectedFile(requestManager.getFileName(lastSelectedFileURI));
    }

    public void inspectApk() {
        boolean bool;
        try {
            bool = requestManager.doesThisAppRequireGooglePlayServices(lastSelectedFileURI);
        } catch (Exception e) {
            return;
        }

        if (bool)
            infoFromInspectApk = "The selected app needs Google Play Services";
        else
            infoFromInspectApk = "The select app does not need Google Play Services";
    }

    public void changeFragment(Fragment newFrag) {
        // bundle to send the inspect result to the next fragment
        Bundle args = new Bundle();
        args.putString("inspectResult", infoFromInspectApk);
        newFrag.setArguments(args);

        fragNavigator.navigateTo(newFrag, false);
    }
}
