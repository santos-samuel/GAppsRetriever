package com.example.mvpexample.model;

public class Manager {
    private Cache cache = new Cache();

    public int askForNextColor(int colorID) {
        return cache.getNextColor(colorID);
    }

    public String askForNextString(String oldString) {
        return cache.getNextString(oldString);
    }
}
