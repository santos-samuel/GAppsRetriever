package com.example.mvpexample.model;

public class RequestManager {

    private PersistentMemory memory;

    public RequestManager(PersistentMemory persistentMemory) {
        this.memory = persistentMemory;
    }

    public int askForNextColor(int colorID) {
        return memory.getNextColor(colorID);
    }

    public String askForNextString(String oldString) {
        return memory.getNextString(oldString);
    }

    public int retrieveLastColor() {
        return memory.getLastGivenColor();
    }

    public String retrieveLastString() {
        return memory.getLastGivenString();
    }
}
