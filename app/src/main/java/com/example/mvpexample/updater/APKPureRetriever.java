package com.example.mvpexample.updater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class APKPureRetriever {

    public APKPureRetriever() {
        String title ="";
        Document doc;
        try {
            doc = Jsoup.connect("https://apkpure.com/google-play-services/com.google.android.gms/versions").get();
            Elements a = doc.getElementsByClass("ver-info-m");
            title = doc.title();
            System.out.print(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
