package com.example.mvpexample.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.example.mvpexample.BuildConfig;
import com.example.mvpexample.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

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

        boolean needsGooglePlayServices = false;


        //--------------------------- META_DATA --------------------------------//
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
        Bundle metaData = packageInfo.applicationInfo.metaData;

        if (metaData != null)
            needsGooglePlayServices = searchMetadata(metaData); // returns true if there is something in metadata that requires Google Play Services

        if (needsGooglePlayServices)
            return true;

        //--------------------------- ACTIVITIES --------------------------------//
        packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        ActivityInfo[] activities = packageInfo.activities;

        if (activities != null)
            needsGooglePlayServices = searchActivities(activities); // returns true if there is something in activities that requires Google Play Services

        if (needsGooglePlayServices)
            return true;


        //--------------------------- PERMISSIONS --------------------------------//
        packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
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

    public void checkIfGooglePlayServicesIsAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        // Returns status code indicating whether there was an error. Can be one of following:
        // SUCCESS, SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED, SERVICE_DISABLED, SERVICE_INVALID
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mainActivity);

        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d("CONNECTION RESULT", "" + resultCode);
            switch (resultCode) {
                case ConnectionResult.SERVICE_MISSING:                  // 1
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:  // 2
                    showAskUserIfHeWantsToDownloadDialog();
                    break;

                case ConnectionResult.SERVICE_INVALID:                  // 9
                    showDeviceNotSupportedDialog();
                    break;

                case ConnectionResult.SERVICE_DISABLED:                 // 3
                    showGooglePlayServicesIsDisabledDialog();
                    break;

                case ConnectionResult.SERVICE_UPDATING:                 // 18
                    // no problem?
                    GooglePlayServicesUtil.getErrorDialog(resultCode, mainActivity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                    break;

                default:
                    if (!apiAvailability.isUserResolvableError(resultCode)) { // 3 (disabled) or 18 (updating)
                        showDeviceNotSupportedDialog();
                    }
                    break;
            }
        }
        else {                                                          // 0
            showOkDialog();
        }
    }

    private void installAPK(String pathToApk) {
        Log.d("TAG", "Install APK!");

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

    public void showOkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setNeutralButton(R.string.cast_tracks_chooser_dialog_ok, null);

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("Eveything is ok!");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity(mainActivity);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showGooglePlayServicesIsDisabledDialog() { // DISABLED
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent openGPSSettings = new Intent();
                openGPSSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.google.android.gms", null);
                openGPSSettings.setData(uri);
                mainActivity.startActivity(openGPSSettings);
            }
        });
        builder.setNegativeButton(R.string.cast_tracks_chooser_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("The installed app contains features that may not " +
                "work unless you enable Google Play Services App.\n" +
                "Do you want to enable Google Play Services?");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(232, 106, 37));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity(mainActivity);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showAskUserIfHeWantsToDownloadDialog() { // UPDATE_REQUIRED and MISSING
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                downloadGooglePlayServicesMarket();
            }
        });

        builder.setNegativeButton(R.string.cast_tracks_chooser_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("The installed app contains features that may not " +
                "work unless you install/update Google Play Services App.\n" +
                "Do you want to install the latest version of Google Play Services?");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(232, 106, 37));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity(mainActivity);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showDeviceNotSupportedDialog() { // INVALID and !isUserResolvableError
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setNeutralButton(R.string.cast_tracks_chooser_dialog_ok, null);

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("The installed app contains features that may not " +
                "work without Google Play Services, which are not supported by your device.");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity(mainActivity);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void closeActivity(Activity a) {
        a.finish();
    }
}
