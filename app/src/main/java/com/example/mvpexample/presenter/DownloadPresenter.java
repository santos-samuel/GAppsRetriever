package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.DownloadFragment;

public class DownloadPresenter {
    private DownloadFragment downloadFragment;
    private RequestManager requestManager;
    private FragmentNavigator fragNavigator;

    public DownloadPresenter(DownloadFragment downloadFragment, RequestManager requestManager, FragmentNavigator fragNavigator) {
        this.downloadFragment = downloadFragment;
        this.requestManager = requestManager;
        this.fragNavigator = fragNavigator;
    }
}
