package com.example.mvpexample.updater;

import com.example.mvpexample.model.GenericCallback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class APKMirrorRetriever2 implements IGetRequestInfo {
    private RequestStatus mResult;
    private String mError;
    //private static final String DownloadUrl = "https://www.apkmirror.com";

    public APKMirrorRetriever2(DeviceSpecs deviceSpecs, GenericCallback callback) {
        Document doc;
        try {
            for (int i = 1; ; i++) { // iterate page
                doc = Jsoup.connect("https://www.apkmirror.com/uploads/page/"+i+"/?q=google-play-services").get();
                Elements elements = doc.getElementsByClass("appRowVariantTag wrapText");

                if (elements.size() == 0) { // no div with class 'appRowVariantTag wrapText' found (page number exceeded)
                    mError = "No updates found";
                    mResult = RequestStatus.STATUS_OK;
                    callback.onResult(null, null,this);
                    return;
                }

                for (Element e : elements) { // iterate uploads
                    String releaseLink = e.select("a[href]").first().attr("abs:href");
                    System.out.println(releaseLink);

                    Object[] o = verifyIfReleaseSuitsDeviceSpecs(releaseLink, deviceSpecs); // either returns download page link or null

                    if (o == null) {
                        continue; // no variant compatible, check next upload
                    }
                    else {
                        // found compatible variant
                        String releaseDownloadPage = (String) o[0];
                        String versionName = (String) o[1];

                        String downloadLink = getReleaseDownloadLink(releaseDownloadPage);
                        callback.onResult(downloadLink, versionName, this);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            mError = e.getMessage();
            mResult = RequestStatus.STATUS_ERROR;
            callback.onResult(null, null, this);
        }
    }

    private String getReleaseDownloadLink(String releaseDownloadPage) throws IOException {
        Document doc = Jsoup.connect(releaseDownloadPage).get();
        Elements elements = doc.getElementsByClass("btn btn-flat downloadButton");
        String thankYouPage = elements.first().select("a[href]").first().attr("abs:href");

        doc = Jsoup.connect(thankYouPage).get();
        elements = doc.getElementsByClass("notes");
        return elements.select("a[href]").attr("abs:href");
    }

    private String[] verifyIfReleaseSuitsDeviceSpecs(String link, DeviceSpecs deviceSpecs) throws IOException {
        Document doc = Jsoup.connect(link).get();

        Elements elements = doc.getElementsByClass("table-row headerFont");
        elements.remove(0); // skip table header

        for (Element e : elements) { // iterate variants
            Elements variantInfo = e.getElementsByClass("table-cell rowheight addseparator expand pad dowrap");
            List<String> archList = parseArchitecture(variantInfo.get(1).text());
            String release = parseReleaseVersion(variantInfo.get(2).text());

            if (!deviceSpecs.supports(archList, release))
                continue;

            // else, found compatible update
            Element variantElement = variantInfo.get(0);
            String releaseDownloadPage = variantElement.select("a[href]").first().attr("abs:href");
            String versionName = parseVersionName(variantElement.select("a").text());
            return new String[] {releaseDownloadPage, versionName};
        }
        return null;
    }

    private String parseVersionName(String versionName) { // X.X.X (Y-W-Z)
        String[] s = versionName.split(" ");
        return s[0].trim();
    }

    private List<String> parseArchitecture(String s) {
        String[] split = s.split("\\+");
        for (int i = 0; i < split.length; i++)
            split[i] = split[i].trim();
        return Arrays.asList(split);
    }

    private String parseReleaseVersion(String text) { // text = "Android X.Y+"
        String[] minVersionParsed = text.split("\\s"); // {Android, X.Y+}
        return minVersionParsed[1].substring(0, minVersionParsed[1].length() - 1); // X.Y
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