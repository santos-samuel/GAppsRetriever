package com.example.mvpexample.presenter;

import com.example.mvpexample.model.RequestManager;

public class DownloadGPSPresenter {

    private final IDownloadGPSView view;
    private RequestManager requestManager;

    public DownloadGPSPresenter(IDownloadGPSView view, RequestManager requestManager) {
        this.requestManager = requestManager;
        this.view = view;
    }

    public void downloadGPSRequest() {
        requestManager.downloadGooglePlayServices();
    }
}
