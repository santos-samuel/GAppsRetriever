package com.example.mvpexample.presenter;

import android.support.v4.app.Fragment;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;

public class ColorPresenter {

    private IColorView view;
    private RequestManager requestManager;
    private FragmentNavigator fragNavigator;

    public ColorPresenter(IColorView view, RequestManager requestManager, FragmentNavigator fragNavigator) {
        this.view = view;
        this.requestManager = requestManager;
        this.fragNavigator =  fragNavigator;
    }

    public void changeColor(int oldColor) {
        int newColor = requestManager.askForNextColor(oldColor);
        view.updateViewColor(newColor);
    }

    public void changeFragment(Fragment newFrag) {
        fragNavigator.navigateTo(newFrag, false);
    }

    public void retrieveLastColor() {
        view.updateViewColor(requestManager.retrieveLastColor());
    }
}
