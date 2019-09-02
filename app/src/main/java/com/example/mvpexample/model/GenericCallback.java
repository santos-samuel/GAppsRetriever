package com.example.mvpexample.model;

import com.example.mvpexample.updater.IGetRequestInfo;

public interface GenericCallback<T, S, I, J> {
    void onResult(String url, Object versionNameOrCode, String fileName, IGetRequestInfo retriever);
}
