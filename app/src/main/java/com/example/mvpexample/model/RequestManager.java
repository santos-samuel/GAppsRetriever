package com.example.mvpexample.model;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;
import com.example.mvpexample.presenter.DownloadPackPresenter;
import com.example.mvpexample.updater.APKMirrorRetriever2;
import com.example.mvpexample.updater.DeviceSpecs;
import com.example.mvpexample.updater.IGetRequestInfo;
import com.google.android.gms.common.GoogleApiAvailability;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static android.content.Context.DOWNLOAD_SERVICE;

public class RequestManager {

    private static final String PATH_TO_DOWNLOAD = Environment.getExternalStorageDirectory() + "/.aptoide/";
    private static final String GOOGLE_PLAY_SERVICES_LINK_MARKET = "https://perkhidmatan-google-play.en.aptoide.com/";
    private static final String PATH_TO_GPS_APK = Environment.getExternalStorageDirectory() + "/.aptoide/" + Constants.GPS_FILE_NAME;
    private final ContentResolver contentResolver;
    private final Activity mainActivity;
    private PersistentMemory memory;
    private DeviceSpecs deviceSpecs;
    private long downloadID;
    private DownloadPackPresenter activePresenter;

    public RequestManager(PersistentMemory persistentMemory, PackageManager packageManager, ContentResolver contentResolver, Activity activity) {
        this.memory = persistentMemory;
        this.contentResolver = contentResolver;
        this.mainActivity = activity;
        this.deviceSpecs = checkHardwareInfo();
    }

    public int askForNextColor(int colorID) {
        return memory.getNextColor(colorID);
    }

    public String askForNextString(String oldString) {
        return memory.getNextString(oldString);
    }

    public int retrieveLastColor() {
        return memory.getLastGivenColor();
    }

    public String retrieveLastString() {
        return memory.getLastGivenString();
    }

    public boolean doesThisAppRequireGooglePlayServices(Uri apkURI) throws Exception {

        String pathToApk;

        try {
            pathToApk = PathUtil.getPath(mainActivity.getApplicationContext(), apkURI, mainActivity.getContentResolver());
        } catch (URISyntaxException e) {
            Log.d("TAG", "Something went very wrong while getting the path of the selected file.");
            throw new Exception();
        }

        return inspectApkManifest(pathToApk);
    }

    public boolean doesThisAppRequireGooglePlayServices(String packageName) throws PackageManager.NameNotFoundException {

        boolean needsGooglePlayServices = false;


        //--------------------------- META_DATA --------------------------------//
        PackageInfo packageInfo = mainActivity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
        Bundle metaData = packageInfo.applicationInfo.metaData;

        if (metaData != null)
            needsGooglePlayServices = searchMetadata(metaData); // returns true if there is something in metadata that requires Google Play Services

        if (needsGooglePlayServices)
            return true;

        //--------------------------- ACTIVITIES --------------------------------//
        packageInfo = mainActivity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        ActivityInfo[] activities = packageInfo.activities;

        if (activities != null)
            needsGooglePlayServices = searchActivities(activities); // returns true if there is something in activities that requires Google Play Services

        if (needsGooglePlayServices)
            return true;


        //--------------------------- PERMISSIONS --------------------------------//
        packageInfo = mainActivity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        String[] requestedPermissions = packageInfo.requestedPermissions;

        if (requestedPermissions != null)
            needsGooglePlayServices = searchPermissions(requestedPermissions); // returns true if there is something in permissions that requires Google Play Services

        if (needsGooglePlayServices)
            return true;


        return false;
    }

    private boolean searchPermissions(String[] requestedPermissions) {
        // BILLING && READ_GSERVICES
        for (String rp : requestedPermissions) {
            if (rp.equals("com.android.vending.BILLING") || rp.equals("com.google.android.providers.gsf.permission.READ_GSERVICES"))
                return true;
        }

        return false;
    }

    private boolean searchActivities(ActivityInfo[] activities) {
        // AUTH SERVICE
        for (ActivityInfo ai : activities) {
            if (ai.name.equals("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
                return true;
        }

        return false;
    }

    private boolean searchMetadata(Bundle metaData) {
        // LOCATION SERVICES
        Object object = metaData.get("com.google.android.geo.API_KEY");
        if (object != null)
            return true;

        object = metaData.get("com.google.android.maps.v2.API_KEY");
        if (object != null)
            return true;


        // WALLET SERVICES
        object = metaData.get("com.google.android.gms.wallet.api.enabled");
        if (object != null)
            return true;


        // GOOGLE NEARBY
        object = metaData.get("com.google.android.nearby.messages.API_KEY");
        if (object != null)
            return true;


        // SAFETY NET (SURE?)
        object = metaData.get("com.google.android.safetynet.ATTEST_API_KEY");
        if (object != null)
            return true;


        // GOOGLE PLAY GAMES SERVICE (maybe notify about needing both services)
        object = metaData.get("com.google.android.gms.games.APP_ID");
        if (object != null)
            return true;

        return false;
    }

    private boolean inspectApkManifest(String pathToApk) {

        //--------------------------- META_DATA --------------------------------//
        PackageInfo packageInfo = getPackageInfo(pathToApk, PackageManager.GET_META_DATA);
        Bundle metaData = packageInfo.applicationInfo.metaData;

        // LOCATION SERVICES
        Object object = metaData.get("com.google.android.geo.API_KEY");
        if (object != null)
            return true;


        object = metaData.get("com.google.android.maps.v2.API_KEY");
        if (object != null)
            return true;


        // WALLET SERVICES
        object = metaData.get("com.google.android.gms.wallet.api.enabled");
        if (object != null)
            return true;


        // GOOGLE NEARBY
        object = metaData.get("com.google.android.nearby.messages.API_KEY");
        if (object != null)
            return true;


        // SAFETY NET (SURE?)
        object = metaData.get("com.google.android.safetynet.ATTEST_API_KEY");
        if (object != null)
            return true;


        // GOOGLE PLAY GAMES SERVICE (maybe notify about needing both services)
        object = metaData.get("com.google.android.gms.games.APP_ID");
        if (object != null)
            return true;


        //--------------------------- ACTIVITIES --------------------------------//
        packageInfo = getPackageInfo(pathToApk, PackageManager.GET_ACTIVITIES);

        // AUTH SERVICE
        ActivityInfo[] activities = packageInfo.activities;
        for (ActivityInfo ai : activities) {
            if (ai.name.equals("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
                return true;
        }


        //--------------------------- PERMISSIONS --------------------------------//
        packageInfo = getPackageInfo(pathToApk, PackageManager.GET_PERMISSIONS);

        // BILLING && READ_GSERVICES
        String[] requestedPermissions = packageInfo.requestedPermissions;
        for (String rp : requestedPermissions) {
            if (rp.equals("com.android.vending.BILLING") || rp.equals("com.google.android.providers.gsf.permission.READ_GSERVICES"))
                return true;
        }


        return false;
    }

    private PackageInfo getPackageInfo(String pathToApk, int componentInfoConstant) {
        PackageInfo packageArchiveInfo = mainActivity.getPackageManager().getPackageArchiveInfo(pathToApk, componentInfoConstant);

        if (packageArchiveInfo == null) {
            throw new IllegalStateException("Extension APK could not be parsed.");
        }

        return packageArchiveInfo;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public int checkIfGooglePlayServicesIsAvailable() { // should ShowDialogs methods be in view?
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        // Returns status code indicating whether there was an error. Can be one of following:
        // SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED,
        // SERVICE_DISABLED, SERVICE_INVALID
        return apiAvailability.isGooglePlayServicesAvailable(mainActivity);
    }

    private void installAPK(String pathToApk) {
        activePresenter.installApk(pathToApk);
    }

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                //Fetching the download id received with the broadcast
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadID == id) {
                    Log.d("DOWNLOAD", "Download completed.");
                    String fileName = getDownloadFileName(downloadID);
                    if (fileName != null)
                        installAPK(PATH_TO_DOWNLOAD + fileName);
                }

                //mainActivity.unregisterReceiver(onDownloadComplete); // Where should i put this?
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };

    private String getDownloadFileName(long downloadID) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);
        Cursor c = ((DownloadManager) mainActivity.getSystemService(DOWNLOAD_SERVICE)).query(query);
        if (c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                int cut = uriString.lastIndexOf('/');
                if (cut != -1) {
                    return uriString.substring(cut + 1);
                }
            }
        }

        return null;
    }


    public void downloadGooglePlayServicesMarket() {
        Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(GOOGLE_PLAY_SERVICES_LINK_MARKET));

        // Verify it resolves
        PackageManager packageManager = mainActivity.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(goToMarket, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe)
            mainActivity.startActivity(goToMarket);
    }

    public void downloadGooglePlayServicesDirect(String link) {
        Log.d("DOWNLOAD", "downloadGooglePlayServicesDirect");

        mainActivity.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        File file = new File(PATH_TO_GPS_APK);

        try {
            if (file.exists()) {
                Log.d("DELETE", "DELETED PREVIOUS FILE.");
                file.delete();
            }
        } catch (Exception e) {
            Log.d("DELETE", "Something went wrong while trying to delete previous file.");
        }
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link))
                .setTitle("Google Play Services")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                //.setRequiresCharging(false)// Set if charging is required to begin the download
                //.setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        DownloadManager downloadManager= (DownloadManager) mainActivity.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }


    public void deleteApkFileOnStorage() {
        File file = new File(PATH_TO_GPS_APK);
        try {
            file.delete();
            Log.d("DELETE", "GPS.APK has been deleted!");
        } catch (Exception e) {
            Log.d("DELETE", "Something went wrong while trying to delete the apk downloaded file");
        }
    }

    public DeviceSpecs checkHardwareInfo() {
        final String DEBUG_TAG_ARC = "Supported ABIS";

        int deviceApi = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;
        String[] supportedABIS = null;

        Toast.makeText(mainActivity, "API: "+ deviceApi, Toast.LENGTH_SHORT).show();
        Log.d(DEBUG_TAG_ARC, "API: "+deviceApi);


        if(deviceApi < Build.VERSION_CODES.LOLLIPOP ) {
            supportedABIS = new String[]{Build.CPU_ABI, Build.CPU_ABI2};

            for (String s : supportedABIS) {
                Log.d("SUPPORTED_ABIS", s);
            }
        }

        if(deviceApi >= Build.VERSION_CODES.LOLLIPOP ) {
            supportedABIS = Build.SUPPORTED_ABIS;

            for (String s : supportedABIS) {
                Log.d("SUPPORTED_ABIS", s);
            }
        }

        String installedGPSVersionName = null;
        int installedGPSVersionCode = -1;
        try {
            PackageInfo pinfo = mainActivity.getPackageManager().getPackageInfo("com.google.android.gms", 0);
            installedGPSVersionName = parseInstalledGPSVersionName(pinfo.versionName);
            installedGPSVersionCode = pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException ignored) {/* should never happen */}

        return new DeviceSpecs(deviceApi, release, supportedABIS, installedGPSVersionName, installedGPSVersionCode);
    }

    private String parseInstalledGPSVersionName(String versionName) {
        String[] s = versionName.split(" ");
        return s[0].trim();
    }

//    private boolean isToDownloadApkMoreRecentThanInstalledApk(String _toDownloadVersionName) {
//        // we need toDownloadVersionName >= installedGPSVersionName
//        String[] installedVersion = this.deviceSpecs.getInstalledGPSVersionName().split("\\.");
//        String[] toDownloadVersion = _toDownloadVersionName.split("\\.");
//
//        int len = Math.min(installedVersion.length, toDownloadVersion.length);
//
//        for (int i = 0; i < len; i++) {
//            int r1 = Integer.parseInt(installedVersion[i]);
//            int r2 = Integer.parseInt(toDownloadVersion[i]);
//
//            if (r1 < r2)
//                return true;
//            if (r1 == r2)
//                continue;
//            if (r1 > r2)
//                return false;
//        }
//        return true; // always continued
//    }
//
//    private boolean isToDownloadApkMoreRecentThanInstalledApk(int toDownloadVersionCode) {
//        /Log.d("INSTALLED_GPS", String.valueOf(this.deviceSpecs.getInstalledGPSVersionCode()));
//        return toDownloadVersionCode > this.deviceSpecs.getInstalledGPSVersionCode();
//    }

    private final GenericCallback<String, Object, String, IGetRequestInfo> callbackConsideringVersionName =
            new GenericCallback<String, Object, String, IGetRequestInfo>() {
                @Override
                public void onResult(String url, Object toDownloadVersionNameO, String fileName, IGetRequestInfo g) {
                    if (url != null && toDownloadVersionNameO != null && g != null) {
                        String toDownloadVersionName = (String) toDownloadVersionNameO;
                        System.out.println(url);
                        System.out.println(toDownloadVersionName);

                        /*if (!isToDownloadApkMoreRecentThanInstalledApk(toDownloadVersionName))
                            Log.d("TAG", "The version found is not recent");
                        else*/
                        downloadDirect(url, fileName);
                    }
                    else {
                        Log.d("ERROR", g.getmError());
                        Log.d("RESULT", String.valueOf(g.getmResult()));
                    }
                }
            };

    private final GenericCallback<String, Object, String, IGetRequestInfo> callbackConsideringVersionCode =
            new GenericCallback<String, Object, String, IGetRequestInfo>() {
                @Override
                public void onResult(String url, Object toDownloadVersionCodeO, String fileName, IGetRequestInfo g) {
                    if (url != null && toDownloadVersionCodeO != null && fileName != null && g != null) {
                        int toDownloadVersionCode = (int) toDownloadVersionCodeO;
                        System.out.println(url);
                        System.out.println(toDownloadVersionCode);

                        /*if (!isToDownloadApkMoreRecentThanInstalledApk(toDownloadVersionCode))
                            Log.d("TAG", "The version found is not recent");
                        else*/
                            downloadDirect(url, fileName);
                    }
                    else {
                        Log.d("ERROR", g.getmError());
                        Log.d("RESULT", String.valueOf(g.getmResult()));
                    }
                }
            };

    public void downloadDirect(String link, String fileName) {
        Log.d("DOWNLOAD", "downloadGooglePlayServicesDirect");

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        mainActivity.registerReceiver(onDownloadComplete, intentFilter);

        File file = new File(PATH_TO_DOWNLOAD + fileName);

        try {
            if (file.exists()) {
                Log.d("DELETE", "DELETED PREVIOUS FILE.");
                file.delete();
            }
        } catch (Exception e) {
            Log.d("DELETE", "Something went wrong while trying to delete previous file.");
        }
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link))
                .setTitle(fileName)
                .setDescription("Downloading...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverRoaming(true);
        DownloadManager downloadManager= (DownloadManager) mainActivity.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);
    }

//    public void getFromApkMirrorAPI() {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                new APKMirrorRetriever(deviceSpecs, callbackConsideringVersionName);
//            }
//        });
//        executor.shutdown();
//    }

    public void getFromApkMirror(final String query, final String fileName, DownloadPackPresenter downloadPackPresenter) {
        this.activePresenter = downloadPackPresenter;
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                new APKMirrorRetriever2(deviceSpecs, callbackConsideringVersionName, query, fileName);
            }
        });
        executor.shutdown();
    }

//    public void getFromGooglePlayAPI() {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                new GoogleRetriever(mainActivity, callbackConsideringVersionCode);
//            }
//        });
//        executor.shutdown();
//    }

//    public void getFromApkPure() {
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                new APKPureRetriever(deviceSpecs, callbackConsideringVersionCode);
//            }
//        });
//        executor.shutdown();
//    }

//    public void getGPServicesFromApkMirror() {
//        getFromApkMirror("/?q=google-play-services", GPS_FILE_NAME);
//    }
//
//    public void getGooglePackApk(String query, String fileName) {
//        getFromApkMirror(query, fileName);
//    }

//    public void getGooglePackFromApkMirror(DownloadPackPresenter downloadPackPresenter) {
//        activePresenter = downloadPackPresenter;
//        checkHardwareInfo();
//        //getGooglePackApk("/?q=google-services-framework", GSF_FILE_NAME);
//        getGooglePackApk("/?q=google-account-manager", GAM_FILE_NAME);
//        //getGooglePackApk("/?q=google-play-services", GPS_FILE_NAME);
//        //getGooglePackApk("/?q=google-calendar-sync", GCalendarSync_FILE_NAME);
//        //getGooglePackApk("/?q=google-contacts-sync", GContactSync_FILE_NAME);
//        //getGooglePackApk("/?q=google-play-store", GPStore_FILE_NAME);
//
//        System.out.println("Finished");
//        // SETTINGS
//    }
//
//    public void getGooglePackFromApkMirror() {
//        checkHardwareInfo();
//        //getGooglePackApk("/?q=google-services-framework", GSF_FILE_NAME);
//        getGooglePackApk("/?q=google-account-manager", GAM_FILE_NAME);
//        //getGooglePackApk("/?q=google-play-services", GPS_FILE_NAME);
//        //getGooglePackApk("/?q=google-calendar-sync", GCalendarSync_FILE_NAME);
//        //getGooglePackApk("/?q=google-contacts-sync", GContactSync_FILE_NAME);
//        //getGooglePackApk("/?q=google-play-store", GPStore_FILE_NAME);
//
//        System.out.println("Finished");
//        // SETTINGS
//    }
}