package com.example.mvpexample.model;

import com.example.mvpexample.updater.IGetRequestInfo;

public interface GenericCallback<T, S, I> {
    void onResult(String u, Object v, IGetRequestInfo g);
}
