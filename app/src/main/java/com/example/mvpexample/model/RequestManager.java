package com.example.mvpexample.model;

public class RequestManager {
    private PersistentMemory memory = new PersistentMemory();

    public int askForNextColor(int colorID) {
        return memory.getNextColor(colorID);
    }

    public String askForNextString(String oldString) {
        return memory.getNextString(oldString);
    }
}
