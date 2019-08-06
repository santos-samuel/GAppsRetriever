package com.example.mvpexample.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import com.example.mvpexample.R;
import com.example.mvpexample.view.MainActivity;

public class AppListener extends BroadcastReceiver {

    private final RequestManager requestManager;
    private final Activity mainActivity;

    public AppListener(RequestManager requestManager, MainActivity mainActivity) {
        this.requestManager = requestManager;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("INSTALL", "An app has been installed!");

        // Delete installed apk file
        Uri installedApkUri = intent.getData();
        String installedPackageName = installedApkUri.getEncodedSchemeSpecificPart();

        Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals("com.google.android.gms"))
            requestManager.deleteApkFileOnStorage();

        else { // A new app has been installed
            try {
                boolean gpsAvailable = requestManager.checkIfGooglePlayServicesIsAvailable();

                if (!gpsAvailable) {

                    boolean requiresGPS = requestManager.doesThisAppRequireGooglePlayServices(installedPackageName);

                    if (requiresGPS)
                        showAskUserIfHeWantsToDownloadDialog();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (DeviceNotSupportedException e) {
                showDeviceNotSupportedDialog();
            } catch (GooglePlayServicesIsDisabledException e) {
                showGooglePlayServicesIsDisabledDialog();
            }
        }
    }

    private void showGooglePlayServicesIsDisabledDialog() {
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
        dialog.show();
    }

    private void showDeviceNotSupportedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setNeutralButton(R.string.cast_tracks_chooser_dialog_ok, null);

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("The installed app contains features that may not " +
                "work without Google Play Services.\n" +
                "Unfortunately, this device does not support Google Play Services.");

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
        dialog.show();
    }

    private void showAskUserIfHeWantsToDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestManager.downloadGooglePlayServicesMarket();
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
        dialog.show();
    }
}
