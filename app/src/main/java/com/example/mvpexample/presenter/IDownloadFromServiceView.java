package com.example.mvpexample.presenter;

public interface IDownloadFromServiceView {
    void finishActivity();
    void showToastMessage(String m);
    void showAskUserIfHeWantsToDownloadDialog();
    void showDeviceNotSupportedDialog();
    void showGooglePlayServicesIsDisabledDialog();
    void showOkDialog();
}