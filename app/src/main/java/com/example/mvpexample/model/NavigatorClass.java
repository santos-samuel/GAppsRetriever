package com.example.mvpexample.model;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.mvpexample.R;
import com.example.mvpexample.view.MainActivity;

public class NavigatorClass {

    private static FragmentNavigator INSTANCE;

    public NavigatorClass(MainActivity mainActivity) {
        INSTANCE = new FragmentNavigator(mainActivity);
    }

    public static FragmentNavigator getInstance() {
        return INSTANCE;
    } 
}
