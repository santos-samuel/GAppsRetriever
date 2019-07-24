package com.example.mvpexample.model;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.mvpexample.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static android.content.Context.DOWNLOAD_SERVICE;

public class RequestManager {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 90;
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
            Log.d("TAG", "Something went very wrong while getting the path of the selected file");
            throw new Exception();
        }

        PackageInfo info = packageManager.getPackageArchiveInfo(pathToApk, PackageManager.GET_META_DATA);

        if (info == null) {
            throw new IllegalStateException("Extension APK could not be parsed");
        }

        Bundle metaData = info.applicationInfo.metaData;
        if (metaData == null)
            return false; // no metadata in the manifest -> no google gms value declared as well

        Object object = metaData.get("com.google.android.gms.version");
        if (object == null)
            return false; // no gms value declared

        if (object instanceof String)
            Log.d("GMS_METADATA_VALUE", metaData.getString("com.google.android.gms.version"));
        if (object instanceof Integer)
            Log.d("GMS_METADATA_VALUE", String.valueOf(metaData.getInt("com.google.android.gms.version")));

        return true;
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

            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(mainActivity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            else {
                AlertDialog alertDialog =
                        new AlertDialog.Builder(mainActivity, R.style.Theme_AppCompat_Dialog).setMessage(
                                "This device is not supported.")
                                .create();
                alertDialog.show();

                //cleanUpTheMess();
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

    public void downloadGooglePlayServices() {
        mainActivity.registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        /*
        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(onDownloadComplete);
        }
        */


        File file = new File(mainActivity.getExternalFilesDir(null),"Dummy");
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse("https://perkhidmatan-google-play.en.aptoide.com/"))
                .setTitle("Dummy File")// Title of the Download Notification
                .setDescription("Downloading")// Description of the Download Notification
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
        DownloadManager downloadManager= (DownloadManager) mainActivity.getSystemService(DOWNLOAD_SERVICE);
        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
    }



    private long downloadID;
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(mainActivity, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
