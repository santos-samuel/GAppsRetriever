package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.DownloadPackFragment;

public class DownloadGPSPresenter {

    private final IDownloadGPSView view;
    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;

    public DownloadGPSPresenter(IDownloadGPSView view, RequestManager requestManager, FragmentNavigator fragmentNavigator) {
        this.requestManager = requestManager;
        this.view = view;
        this.fragNavigator = fragmentNavigator;
    }

    public void downloadGPSRequestMarket() {
        requestManager.downloadGooglePlayServicesMarket();
    }

    public void downloadGPSRequestDirect() {
        requestManager.downloadGooglePlayServicesDirect(null);
    }

    public void downloadGPSFromAPKMirror() {
        requestManager.checkHardwareInfo(); // sets device specs
        //requestManager.getGPServicesFromApkMirror();
    }

    public void downloadFromGoogleAPI() {
        requestManager.checkHardwareInfo(); // sets device specs
        //requestManager.getFromGooglePlayAPI();
    }

    public void downloadFromAPKPure() {
        requestManager.checkHardwareInfo(); // sets device specs
        //requestManager.getFromApkPure();
    }

    public void downloadGoogleAppsPack() {
        fragNavigator.navigateTo(new DownloadPackFragment(), false);
    }
}
