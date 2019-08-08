package com.example.mvpexample.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.example.mvpexample.R;
import com.example.mvpexample.model.DeviceNotSupportedException;
import com.example.mvpexample.model.GooglePlayServicesIsDisabledException;
import com.example.mvpexample.model.RequestManager;


public class AppListenerActivity extends AppCompatActivity {

    private RequestManager requestManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String installedPackageName = getIntent().getExtras().getString("installedPackageName");

        this.requestManager = new RequestManager(null, getPackageManager(), getContentResolver(), this);

        Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals("com.google.android.gms")) {
            //requestManager.deleteApkFileOnStorage();
        }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent openGPSSettings = new Intent();
                openGPSSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.google.android.gms", null);
                openGPSSettings.setData(uri);
                startActivity(openGPSSettings);
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
                closeActivity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void closeActivity() {
        this.finish();
    }

    private void showDeviceNotSupportedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void showAskUserIfHeWantsToDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                closeActivity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}