package com.example.mvpexample.presenter;

import com.example.mvpexample.model.Manager;

public class ColorPresenter {

    private IColorView view;
    private Manager manager = new Manager();

    public ColorPresenter(IColorView view) {
        this.view = view;
    }

    public void changeColor(int oldColor) {
        int newColor = manager.askForNextColor(oldColor);
        view.updateViewColor(newColor);
    }
}
