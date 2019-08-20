package com.example.mvpexample.updater;

import com.example.mvpexample.model.GenericCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class APKPureRetriever implements IGetRequestInfo {

    private RequestStatus mResult;
    private String mError;

    public APKPureRetriever(DeviceSpecs deviceSpecs, GenericCallback<String, Object, IGetRequestInfo> callbackConsideringVersionName) {
        try {
            Document doc = Jsoup.connect("https://apkpure.com/google-play-services/com.google.android.gms/versions").get();
            Elements elements = doc.getElementsByClass("ver-info");
            for (Element e : elements) {
                String versionText = e.select("div.ver-info-top").text();
                Elements plusInfo = e.select("div.ver-info-m").select("p");
                String minApiText = plusInfo.get(1).text();
                String minArchText = plusInfo.get(4).text();
                String releaseDownloadPage = plusInfo.get(7).select("a[href]").first().attr("abs:href");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
