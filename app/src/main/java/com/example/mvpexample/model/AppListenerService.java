package com.example.mvpexample.model;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.mvpexample.R;
import com.example.mvpexample.view.AppListenerActivity;
import com.example.mvpexample.view.MainActivity;

public class AppListenerService extends Service {

    private final MainActivity mainActivity;
    private final RequestManager requestManager;
    private BroadcastReceiver appListener;

    public AppListenerService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.requestManager = mainActivity.getRequestManager();
    }

    public AppListenerService() {
        this.mainActivity = new MainActivity();
        this.requestManager = new RequestManager(null, null, null, null);
    }

    public void notifyAppInstalled(String installedPackageName) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), AppListenerService.class);
        notificationIntent.putExtra("mytype", "simple" + 10); //not required, but used in this example.
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 10, notificationIntent, 0);
        //Create a new notification. The construction Notification(int icon, CharSequence tickerText, long when) is deprecated.
        //If you target API level 11 or above, use Notification.Builder instead
        //With the second parameter, it would show a marquee
        Notification noti = new NotificationCompat.Builder(getApplicationContext(), "test_channel_01")
                .setSmallIcon(R.drawable.aptoide_icon)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setWhen(System.currentTimeMillis())  //When the event occurred, now, since noti are stored by time.
                .setContentTitle("Marquee or Title")   //Title message top row.
                .setContentText("Message, this has only a small icon.")  //message when looking at the notification, second row
                .setContentIntent(contentIntent)  //what activity to open.
                .setAutoCancel(true)   //allow auto cancel when pressed.
                .setChannelId("test_channel_02")
                .build();  //finally build and return a Notification.

        //Show the notification
        nm.notify(10, noti);




        Intent intent = new Intent(this, AppListenerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("installedPackageName", installedPackageName);
        startActivity(intent);

        /*// Delete installed apk file
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
        }*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startListening();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("EXIT", "ondestroy!");

        Intent broadcastIntent = new Intent(this, AppListenerRestarter.class);
        sendBroadcast(broadcastIntent);

        stopListening();

    }

    private void stopListening() {
        unregisterReceiver(appListener);
        this.appListener = null;
    }

    public void startListening() {
        this.appListener = new AppListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        registerReceiver(appListener, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
