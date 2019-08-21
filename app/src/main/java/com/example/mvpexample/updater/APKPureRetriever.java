package com.example.mvpexample.updater;

import com.example.mvpexample.model.GenericCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class APKPureRetriever implements IGetRequestInfo {

    private RequestStatus mResult;
    private String mError;

    public APKPureRetriever(DeviceSpecs deviceSpecs, GenericCallback<String, Object, IGetRequestInfo> callbackConsideringVersionName) {
        try {
            Document doc = Jsoup.connect("https://apkpure.com/google-play-services/com.google.android.gms/versions").get();
            Elements elements = doc.getElementsByClass("ver-info");

            for (Element e : elements) { // iterate GPS versions
                int versionCode = parseVersionCode(e.select("div.ver-info-top").text());
                Elements plusInfo = e.select("div.ver-info-m").select("p");
                int minApi = parseMinApi(plusInfo.get(1).text());
                final String minArchText = parseArchitecture(plusInfo.get(4).text());
                String releaseDownloadPage = plusInfo.get(7).select("a[href]").first().attr("abs:href");


                ArrayList<String> archList = new ArrayList<String>() {{ add(minArchText); }};

                if ( deviceSpecs.supports(archList, minApi) ) {
                    String downloadLink = getDownloadLinkFromDownloadPage(releaseDownloadPage);
                    callbackConsideringVersionName.onResult(downloadLink, versionCode, this);
                    return;
                }
                else
                    continue; // not compatible, check next
            }

            mError = "No updates found";
            mResult = RequestStatus.STATUS_OK;
            callbackConsideringVersionName.onResult(null, null,this);

        } catch (IOException | NumberFormatException e) {
            mError = e.getMessage();
            mResult = RequestStatus.STATUS_ERROR;
            callbackConsideringVersionName.onResult(null, null, this);
        }
    }

    private String getDownloadLinkFromDownloadPage(String releaseDownloadPage) throws IOException {
        Document doc = Jsoup.connect(releaseDownloadPage).get();
        Element element = doc.getElementsByClass("fast-download-box").get(0);
        return element.select("a#download_link").attr("abs:href");
    }

    private String parseArchitecture(String text) {
        // example: Architecture: arm64-v8a
        String[] s = text.split(" ");
        return s[1].trim();
    }

    private int parseMinApi(String text) {
        // example: Requires Android: Android 6.0+ (M, API 23)
        String s = "API "; // next to this word is the API version

        int apiIdx = text.indexOf(s);
        int numberApiStartIndex = apiIdx + s.length(); // first API number
        int parenIndex = text.indexOf(")", numberApiStartIndex);

        String finalApi = text.substring(numberApiStartIndex, parenIndex);
        return Integer.parseInt(finalApi);
    }

    private int parseVersionCode(String text) {
        String[] s = text.split(" ");
        // [google, play, services, version, versionname, versionCode]
        String versionCodeWithParen = s[s.length-1];
        String versionCode = versionCodeWithParen.substring(1, versionCodeWithParen.length() - 1);// remove parentheses
        return Integer.parseInt(versionCode);
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
