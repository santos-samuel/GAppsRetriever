package com.example.mvpexample.presenter;

public interface IApkAnalyzerView {
    void updateSelectedFile(String newFileName);
    void showIfAppNeedsGooglePlayServices(String info, boolean bool);
}
