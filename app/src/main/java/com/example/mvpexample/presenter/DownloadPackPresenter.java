package com.example.mvpexample.presenter;

import android.content.pm.PackageManager;
import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;

public class DownloadPackPresenter {

    private final IDownloadPackView view;
    private final RequestManager requestManager;
    private final FragmentNavigator fragNavigator;
    private int currentIndexOfAppBeeingInstalled;
    private String latestPathSeen;
    private PackageManager packageManager;

    public DownloadPackPresenter(IDownloadPackView view, RequestManager requestManager, FragmentNavigator fragmentNavigator) {
        this.requestManager = requestManager;
        this.view = view;
        this.fragNavigator = fragmentNavigator;
        currentIndexOfAppBeeingInstalled = 0;

        view.initCheckBoxes();
    }

    public void downloadNext() {
        if (currentIndexOfAppBeeingInstalled >= 6) {
            view.toogleProgressBar();
            view.setProgressText("Done!");

            view.showSettingsDialog();
            return;
        }

        if (checkIfAppIsInstalled()) {
            downloadNext();
            return;
        }

        view.setProgressText("Searching for latest compatible App...");
        requestManager.getFromApkMirror(Constants.appsToInstallQuery.get(currentIndexOfAppBeeingInstalled), Constants.appsToInstallFileName.get(currentIndexOfAppBeeingInstalled), this);
    }

    private boolean checkIfAppIsInstalled() {
        try {
            packageManager.getPackageInfo(Constants.appsPackageNames.get(currentIndexOfAppBeeingInstalled), 0);
            view.markAsChecked(currentIndexOfAppBeeingInstalled);
            currentIndexOfAppBeeingInstalled++;
            return true;

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void installApk(String pathToApk) {
        latestPathSeen = pathToApk;
        view.setProgressText("Installing...");
        view.installApk(pathToApk);
    }

    public void notifyPackageManagerClosed() {
        try {
            packageManager.getPackageInfo(Constants.appsPackageNames.get(currentIndexOfAppBeeingInstalled), 0);

            view.markAsChecked(currentIndexOfAppBeeingInstalled);
            currentIndexOfAppBeeingInstalled++;

            downloadNext();
        } catch (PackageManager.NameNotFoundException e) {
            view.showUserCancelledInstallationDialog();
        }
    }

    public void startDownloadingGoogleApps(PackageManager packageManager) {
        this.packageManager = packageManager;
        view.toogleProgressBar();
        downloadNext();
    }

    public void proceed() {
        installApk(latestPathSeen);
    }
}
