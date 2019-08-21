package com.example.mvpexample.updater;

import com.example.mvpexample.model.Constants;
import com.example.mvpexample.model.GenericCallback;
import com.example.mvpexample.updater.APKMirror.*;
import com.google.gson.Gson;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APKMirrorRetriever implements IGetRequestInfo {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String BaseUrl = "https://www.apkmirror.com/wp-json/apkm/v1/";
    private static final String DownloadUrl = "https://www.apkmirror.com";
    private static final String AppExists = "app_exists/";
    private static final String User = "api-apkupdater";
    private static final String Token = "rm5rcfruUjKy04sMpyMPJXW8";
    private DeviceSpecs deviceSpecs;
    private String mError;
    private RequestStatus mResult;

    public APKMirrorRetriever(DeviceSpecs deviceSpecs, GenericCallback callback) {
        this.deviceSpecs = deviceSpecs;

        // Create the OkHttp client
        OkHttpClient client = getOkHttpClient();
        if (client == null) {
            mError = "Unable to get OkHttpError";
            mResult = RequestStatus.STATUS_ERROR;
            callback.onResult(null, null,this);
            return;
        }

        // Build the json object for the request
        List<String> pnames = new ArrayList<>();
        pnames.add(Constants.GOOGLE_GPS_PACKAGE_NAME);


        AppExistsRequest json = new AppExistsRequest(
                pnames,
                null
        );

        RequestBody body = RequestBody.create(JSON, new Gson().toJson(json));
        final Request request = new Request.Builder()
                .url(BaseUrl + AppExists)
                .header("User-Agent", "APKUpdater-v1.0.0")
                .post(body)
                .header("Authorization", Credentials.basic(User, Token))
                .build();

        // Perform request
        AppExistsResponseApk appExistsResponseApk;
        try {
            Response r = client.newCall(request).execute();
            appExistsResponseApk = parseResponse(r.body().string());
        } catch (Exception e) {
            mError = "Request failure: " + e;
            mResult = RequestStatus.STATUS_ERROR;
            callback.onResult(null, null,this);
            return;
        }

        if (appExistsResponseApk != null) { /* TO DO - VERSION NAME*/
            callback.onResult(appExistsResponseApk.getLink(), null, this);
            return;
        }
        else
            callback.onResult(null, null,this);
    }

    private AppExistsResponseApk parseResponse(String body) {
        try {
            // Convert response json to object
            AppExistsResponse r = new Gson().fromJson(body, AppExistsResponse.class);

            // Check if request was successful (Code 200)
            if (r.getStatus() != 200) {
                mError = "Request not successful: " + r.getStatus();
                mResult = RequestStatus.STATUS_ERROR;
                return null;
            }

            AppExistsResponseData data = getData(r, Constants.GOOGLE_GPS_PACKAGE_NAME);

            AppExistsResponseApk selectedApk = null;

            if (data != null) {
                for (AppExistsResponseApk apk : data.getApks()) {
                    if ( deviceSpecs.supports(apk.getArches(), Integer.valueOf(apk.getMinapi())) ) {
                        selectedApk = apk;
                        break;
                    }
                }
            } else {
                mError = "No updates found.";
                mResult = RequestStatus.STATUS_OK;
                return null;
            }

            return selectedApk;

        } catch(Exception e) {
            mError = "No updates found.";
            mResult = RequestStatus.STATUS_OK;
            return null;
        }
    }

    private AppExistsResponseData getData(
            AppExistsResponse response,
            String pname
    ) {
        for (AppExistsResponseData data : response.getData()) {
            if (data.getPname().equals(pname)) {
                return data;
            }
        }
        return null;
    }

    private OkHttpClient getOkHttpClient(
    ) {
        try {
            final TrustManager[] trust = new TrustManager[] {
                    new X509TrustManager() {
                        @Override public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trust, new java.security.SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(context.getSocketFactory(), (X509TrustManager)trust[0])
                    .build();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public RequestStatus getmResult() {
        return mResult;
    }

    @Override
    public String getmError() {
        return mError;
    }
}