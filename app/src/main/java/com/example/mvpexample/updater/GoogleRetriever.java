package com.example.mvpexample.updater;

import android.content.Context;
import android.util.Log;

import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.GenericCallback;
import com.example.mvpexample.updater.GoogleAPI.GooglePlayUtil;
import com.github.yeriomin.playstoreapi.AndroidAppDeliveryData;
import com.github.yeriomin.playstoreapi.AndroidAppPatchData;
import com.github.yeriomin.playstoreapi.AppFileMetadata;
import com.github.yeriomin.playstoreapi.DetailsResponse;
import com.github.yeriomin.playstoreapi.DocV2;
import com.github.yeriomin.playstoreapi.DocumentDetails;
import com.github.yeriomin.playstoreapi.GooglePlayAPI;
import com.github.yeriomin.playstoreapi.UploadDeviceConfigResponse;

import java.io.IOException;

public class GoogleRetriever implements IGetRequestInfo {
    private static GooglePlayAPI mApi = null;
    private String mError;
    private RequestStatus mResultCode;

    public GoogleRetriever(Context context, GenericCallback callback) {

        try {
            mApi = GooglePlayUtil.getApi(context);

            if (mApi == null) {
                mError = "Unable to get GooglePlayApi";
                mResultCode = RequestStatus.STATUS_ERROR;
                callback.onResult(null, null, this);
                return;
            }

            //UploadDeviceConfigResponse uploadDeviceConfigResponse = mApi.uploadDeviceConfig();

            DetailsResponse response = mApi.details(Constants.GOOGLE_GPS_PACKAGE_NAME);
            if (response == null) {
                mError = "Response is null";
                mResultCode = RequestStatus.STATUS_ERROR;
                return;
            }

            final DocV2 details = response.getDocV2();
            DocumentDetails details1 = details.getDetails();
            final int versionCode = details.getDetails().getAppDetails().getVersionCode();
            Log.d("GOOGLE APK VERSION", String.valueOf(versionCode));

            AndroidAppDeliveryData data = GooglePlayUtil.getAppDeliveryData(
                    GooglePlayUtil.getApi(context),
                    Constants.GOOGLE_GPS_PACKAGE_NAME
            );

            if (data == null) {
                mError = "Couldn't retrieve data";
                mResultCode = RequestStatus.STATUS_ERROR;
                callback.onResult(null, null, this);
                return;
            }

            AndroidAppPatchData patchData = data.getPatchData();
            int additionalFileCount = data.getAdditionalFileCount();
            if (additionalFileCount > 0) {
                AppFileMetadata additionalFile = data.getAdditionalFile(0);
                System.out.println("");
            }

            String apkUrl = data.getDownloadUrl();
            callback.onResult(apkUrl, versionCode, this);

        } catch (IOException e) {
            mError = String.valueOf(e);
            mResultCode = RequestStatus.STATUS_ERROR;
            callback.onResult(null, null, this);
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
