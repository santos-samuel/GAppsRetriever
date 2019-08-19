package com.example.mvpexample.updater;

import android.content.Context;

import com.example.mvpexample.model.GenericCallback;
import com.example.mvpexample.updater.GoogleAPI.GooglePlayUtil;
import com.github.yeriomin.playstoreapi.AndroidAppDeliveryData;
import com.github.yeriomin.playstoreapi.GooglePlayAPI;
import java.io.IOException;

public class GoogleRetriever implements IGetRequestInfo {
    private static GooglePlayAPI mApi = null;
    private String mError;
    private RequestStatus mResultCode;

    public GoogleRetriever(Context context, GenericCallback<String, IGetRequestInfo> callback) {

        try {
            mApi = GooglePlayUtil.getApi(context);

            if (mApi == null) {
                mError = "Unable to get GooglePlayApi";
                mResultCode = RequestStatus.STATUS_ERROR;
                return;
            }

            AndroidAppDeliveryData data = GooglePlayUtil.getAppDeliveryData(
                    GooglePlayUtil.getApi(context),
                    "com.google.android.gms"
            );

            if (data == null) {
                mError = "Couldn't retrieve data";
                mResultCode = RequestStatus.STATUS_ERROR;
                return;
            }

            String apkUrl = data.getDownloadUrl();
            callback.onResult(apkUrl, this);

        } catch (IOException e) {
            mError = String.valueOf(e);
            mResultCode = RequestStatus.STATUS_ERROR;
        }
    }

    @Override
    public RequestStatus getmResult() {
        return mResultCode;
    }

    @Override
    public String getmError() {
        return mError;
    }
}
