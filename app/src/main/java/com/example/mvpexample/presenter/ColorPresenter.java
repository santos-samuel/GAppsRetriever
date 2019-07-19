package com.example.mvpexample.presenter;

import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.NavigatorClass;
import com.example.mvpexample.model.RequestManager;
import com.example.mvpexample.view.StringFragment;

public class ColorPresenter {

    private IColorView view;
    private RequestManager requestManager = new RequestManager();
    private FragmentNavigator fragNavigator = NavigatorClass.getInstance();

    public ColorPresenter(IColorView view) {
        this.view = view;
    }

    public void changeColor(int oldColor) {
        int newColor = requestManager.askForNextColor(oldColor);
        view.updateViewColor(newColor);
    }

    public void changeFragment() {
        fragNavigator.navigateTo(new StringFragment(), false);
    }
}
