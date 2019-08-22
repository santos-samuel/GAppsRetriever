package com.example.mvpexample.presenter;

public interface ICheckGooglePlayServicesView {
    void showIfSelectedAppNeedsGooglePlayServices(String info);
    void showToastMessage(String m);
}
