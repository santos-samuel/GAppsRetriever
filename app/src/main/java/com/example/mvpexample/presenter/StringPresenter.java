package com.example.mvpexample.presenter;

import com.example.mvpexample.model.RequestManager;

public class StringPresenter {

    private IStringView view;
    private RequestManager requestManager = new RequestManager();

    public StringPresenter(IStringView view) {
        this.view = view;
    }

    public void changeString(String oldString) {
        String newString = requestManager.askForNextString(oldString);
        view.updateViewString(newString);
    }
}
