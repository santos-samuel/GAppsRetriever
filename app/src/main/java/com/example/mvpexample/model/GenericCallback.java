package com.example.mvpexample.model;

import com.example.mvpexample.updater.IGetRequestInfo;

public interface GenericCallback<T, I> {
    void onResult(String string, IGetRequestInfo g);
}
