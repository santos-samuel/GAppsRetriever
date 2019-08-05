package com.example.mvpexample.model;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.mvpexample.BuildConfig;
import com.example.mvpexample.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class RequestManager {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 90;

    private static final String GPS_FILE_NAME = "gps.apk";
    private static final String PATH_TO_GPS_APK = Environment.getExternalStorageDirectory() + "/.aptoide/" + GPS_FILE_NAME;

    private static final String GOOGLE_PLAY_SERVICES_LINK_MARKET = "https://perkhidmatan-google-play.en.aptoide.com/";

    // TO DO
    private static final String GOOGLE_PLAY_SERVICES_LINK_DIRECT = "https://www.apkmirror.com/wp-content/themes/APKMirror/download.php?id=762848";

    private final PackageManager packageManager;
    private final ContentResolver contentResolver;
    private final Activity mainActivity;
    private PersistentMemory memory;

    public RequestManager(PersistentMemory persistentMemory, PackageManager packageManager, ContentResolver contentResolver, Activity activity) {
        this.memory = persistentMemory;
        this.packageManager = packageManager;
        this.contentResolver = contentResolver;
        this.mainActivity = activity;
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

        //--------------------------- META_DATA --------------------------------//
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
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
        packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

        // AUTH SERVICE
        ActivityInfo[] activities = packageInfo.activities;
        for (ActivityInfo ai : activities) {
            if (ai.name.equals("com.google.android.gms.auth.api.signin.internal.SignInHubActivity"))
                return true;
        }


        //--------------------------- PERMISSIONS --------------------------------//
        packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

        // BILLING && READ_GSERVICES
        String[] requestedPermissions = packageInfo.requestedPermissions;
        for (String rp : requestedPermissions) {
            if (rp.equals("com.android.vending.BILLING") || rp.equals("com.google.android.providers.gsf.permission.READ_GSERVICES"))
                return true;
        }

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
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(pathToApk, componentInfoConstant);

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

    public boolean checkIfGooglePlayServicesIsAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        //int resultCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity); //Returns status code indicating whether there was an error. Can be one of following in ConnectionResult: SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d("CONNECTION RESULT", "" + resultCode);
            switch (resultCode) {
                case ConnectionResult.SERVICE_MISSING:
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                case ConnectionResult.SERVICE_INVALID:
                    AlertDialog alertDialog =
                            new AlertDialog.Builder(mainActivity, R.style.Theme_AppCompat_Dialog).setMessage(
                                    "GPS is not installed/updated/valid!")
                                    .create();
                    alertDialog.show();
                    // install test apk
                    //File GPSApk = new File("/storage/sdcard0/Download/GooglePlayServices.apk");
                    //installAPK(GPSApk);
                    break;

                default:
                    if (apiAvailability.isUserResolvableError(resultCode)) {
                        apiAvailability.getErrorDialog(mainActivity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    }

                    else {
                        alertDialog =
                                new AlertDialog.Builder(mainActivity, R.style.Theme_AppCompat_Dialog).setMessage(
                                        "This device is not supported.")
                                        .create();
                        alertDialog.show();
                    }
                    break;

            }

            return false;
        }

        AlertDialog alertDialog =
                new AlertDialog.Builder(mainActivity, R.style.Theme_AppCompat_Dialog).setMessage(
                        "Everything is ok!")
                        .create();
        alertDialog.show();

        return true;
    }

    private void installAPK(String pathToApk) {
        Log.d("TAG", "Install APK!");
        /*Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");

        Uri apkURI = FileProvider.getUriForFile(mainActivity, mainActivity.getApplicationContext().getPackageName() + ".provider", new File(pathToApk));
        intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mainActivity.startActivity(intent);*/

        /*Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        Uri apkURI = FileProvider.getUriForFile(mainActivity, mainActivity.getApplicationContext().getPackageName() + ".provider", new File(pathToApk));
        intent.setDataAndType(apkURI, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.startActivity(intent);*/

        /*Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(pathToApk)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mainActivity.startActivity(intent);*/


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mainActivity, BuildConfig.APPLICATION_ID + ".provider", new File(pathToApk));
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            mainActivity.startActivity(intent);
        } else {
            Uri apkUri = Uri.fromFile(new File(pathToApk));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainActivity.startActivity(intent);
        }
    }

    private long downloadID;
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Log.d("DOWNLOAD", "Download GPS completed.");
                Toast.makeText(mainActivity, "Download Completed", Toast.LENGTH_SHORT).show();
                installAPK(PATH_TO_GPS_APK);
            }

            //mainActivity.unregisterReceiver(onDownloadComplete); // Where should i put this?
        }
    };

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

    public void downloadGooglePlayServicesDirect() {
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
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(GOOGLE_PLAY_SERVICES_LINK_DIRECT))
                .setTitle("Google Play Services")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                //.setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
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
}
