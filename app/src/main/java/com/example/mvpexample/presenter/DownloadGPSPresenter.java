package com.example.mvpexample.presenter;

import com.example.mvpexample.model.RequestManager;

public class DownloadGPSPresenter {

    private final IDownloadGPSView view;
    private RequestManager requestManager;

    public DownloadGPSPresenter(IDownloadGPSView view, RequestManager requestManager) {
        this.requestManager = requestManager;
        this.view = view;
    }

    public void downloadGPSRequestMarket() {
        requestManager.downloadGooglePlayServicesMarket();
    }

    public void downloadGPSRequestDirect() {
        requestManager.downloadGooglePlayServicesDirect(null);
    }

    public void downloadFromAPKMirror() {
        requestManager.checkHardwareInfo(); // sets device specs
        requestManager.getFromApkMirror();
    }

    public void downloadFromGoogleAPI() {
        requestManager.checkHardwareInfo(); // sets device specs
        requestManager.getFromGooglePlayAPI();
    }

    public void downloadFromAPKPure() {
        requestManager.checkHardwareInfo(); // sets device specs
        requestManager.getFromApkPure();
    }
}
