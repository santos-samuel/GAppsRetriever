package com.example.mvpexample.presenter;

import com.example.mvpexample.model.Manager;

public class StringPresenter {

    private IStringView view;
    private Manager manager = new Manager();

    public StringPresenter(IStringView view) {
        this.view = view;
    }

    public void changeString(String oldString) {
        String newString = manager.askForNextString(oldString);
        view.updateViewString(newString);
    }
}
