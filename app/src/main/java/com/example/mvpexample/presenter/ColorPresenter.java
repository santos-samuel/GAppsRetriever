package com.example.mvpexample.presenter;

import com.example.mvpexample.model.RequestManager;

public class ColorPresenter {

    private IColorView view;
    private RequestManager requestManager = new RequestManager();

    public ColorPresenter(IColorView view) {
        this.view = view;
    }

    public void changeColor(int oldColor) {
        int newColor = requestManager.askForNextColor(oldColor);
        view.updateViewColor(newColor);
    }
}
