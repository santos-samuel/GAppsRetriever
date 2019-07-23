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

        Uri selectedFile = data.getData();
        lastSelectedFileURI = selectedFile;
        view.updateSelectedFile(selectedFile.getPath());
    }

    public void inspectApk() {
        requestManager.doesThisAppRequireGooglePlayServices(lastSelectedFileURI);
    }
}
