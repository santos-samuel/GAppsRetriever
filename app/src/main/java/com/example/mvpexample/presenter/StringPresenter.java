package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.ColorFragment;

public class StringPresenter {

    private IStringView view;
    private RequestManager requestManager;
    private FragmentNavigator fragNavigator;

    public StringPresenter(IStringView view, RequestManager requestManager, FragmentNavigator fragNavigator) {
        this.view = view;
        this.requestManager = requestManager;
        this.fragNavigator = fragNavigator;
    }

    public void changeString(String oldString) {
        String newString = requestManager.askForNextString(oldString);
        view.updateViewString(newString);
    }

    public void changeFragment() {
        fragNavigator.navigateTo(new ColorFragment(), true);
    }

    public void retrieveLastString() {
        view.updateViewString(requestManager.retrieveLastString());
    }
}
