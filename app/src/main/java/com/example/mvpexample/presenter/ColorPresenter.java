package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.StringFragment;

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

    public void changeFragment() {
        fragNavigator.navigateTo(new StringFragment(), false);
    }

    public void retrieveLastColor() {
        view.updateViewColor(requestManager.retrieveLastColor());
    }
}
