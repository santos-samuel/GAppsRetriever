package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.NavigatorClass;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.ColorFragment;

public class StringPresenter {

    private IStringView view;
    private RequestManager requestManager = new RequestManager();
    private FragmentNavigator fragNavigator = NavigatorClass.getInstance();

    public StringPresenter(IStringView view) {
        this.view = view;
    }

    public void changeString(String oldString) {
        String newString = requestManager.askForNextString(oldString);
        view.updateViewString(newString);
    }

    public void changeFragment() {
        fragNavigator.navigateTo(new ColorFragment(), true);
    }
}
