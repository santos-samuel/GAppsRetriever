package com.example.mvpexample.presenter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.example.mvpexample.model.RequestManager;

public class ApkAnalyzerPresenter {

    private IApkAnalyzerView view;
    private RequestManager requestManager;
    private Uri lastSelectedFileURI;

    public ApkAnalyzerPresenter(IApkAnalyzerView view, RequestManager requestManager) {
        this.view = view;
        this.requestManager = requestManager;
    }

    public void processSelectedFile(Intent data) {
        Log.d("PROCESS", "File Selected");

        lastSelectedFileURI = data.getData();
        view.updateSelectedFile(requestManager.getFileName(lastSelectedFileURI));
    }

    public void inspectApk() {
        boolean bool = false;
        try {
            bool = requestManager.doesThisAppRequireGooglePlayServices(lastSelectedFileURI);
        } catch (Exception e) {
            return;
        }
        String info;

        if (bool)
            info = "This app needs Google Play Services";
        else
            info = "This app does not need Google Play Services";

        view.showIfAppNeedsGooglePlayServices(info, bool);
    }

    public void checkGPSAvailability() {
        requestManager.checkIfGooglePlayServicesIsAvailable();
    }

    public void downloadGPSRequest() {
        requestManager.downloadGooglePlayServices();
    }
}
