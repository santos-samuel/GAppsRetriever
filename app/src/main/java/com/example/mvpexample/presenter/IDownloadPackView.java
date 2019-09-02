package com.example.mvpexample.presenter;

public interface IDownloadPackView {
    void installApk(String pathToApk);
    void initCheckBoxes();
    void markAsChecked(int i);
    void toogleProgressBar();
    void setProgressText(String text);
    void showUserCancelledInstallationDialog();
    void showSettingsDialog();
}
