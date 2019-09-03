package com.example.mvpexample.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mvpexample.BuildConfig;
import com.example.mvpexample.R;
import com.example.mvpexample.model.Constants;
import com.example.mvpexample.presenter.DownloadPackPresenter;
import com.example.mvpexample.presenter.IDownloadPackView;
import java.io.File;

public class DownloadPackFragment extends Fragment implements IDownloadPackView {

    private DownloadPackPresenter presenter;
    private CheckBox checkGSF;
    private CheckBox checkGAccManager;
    private CheckBox checkGPServices;
    private CheckBox checkCalendarSync;
    private CheckBox checkContactSync;
    private CheckBox checkGPStore;
    private ProgressBar progressBar;
    private TextView textViewProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_pack_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.checkGSF = view.findViewById(R.id.checkGSF);
        this.checkGAccManager = view.findViewById(R.id.checkGAccManager);
        this.checkGPServices = view.findViewById(R.id.checkGPServices);
        this.checkCalendarSync = view.findViewById(R.id.checkCalendarSync);
        this.checkContactSync = view.findViewById(R.id.checkContactSync);
        this.checkGPStore = view.findViewById(R.id.checkGPStore);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.textViewProgressBar = view.findViewById(R.id.textViewProgressBar);

        presenter = new DownloadPackPresenter(this, ((MainActivity) getActivity()).getRequestManager(), ((MainActivity) getActivity()).getFragNavigator());

        presenter.startDownloadingGoogleApps(getContext().getPackageManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.INSTALL_REQUEST_CODE) {
            presenter.notifyPackageManagerClosed();
        }
    }

    @Override
    public void installApk(String pathToApk) {
        Log.d("TAG", "Install APK!");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", new File(pathToApk));
                Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                intent.setData(apkUri);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, 51);
            } else {
                Uri apkUri = Uri.fromFile(new File(pathToApk));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, Constants.INSTALL_REQUEST_CODE);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initCheckBoxes() {
        this.checkGSF.setEnabled(false);
        this.checkGAccManager.setEnabled(false);
        this.checkGPServices.setEnabled(false);
        this.checkCalendarSync.setEnabled(false);
        this.checkContactSync.setEnabled(false);
        this.checkGPStore.setEnabled(false);
        this.progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void markAsChecked(int i) {
        switch (i) {
            case 0:
                this.checkGSF.setChecked(true);
                break;
            case 1:
                this.checkGAccManager.setChecked(true);
                break;
            case 2:
                this.checkGPServices.setChecked(true);
                break;
            case 3:
                this.checkCalendarSync.setChecked(true);
                break;
            case 4:
                this.checkContactSync.setChecked(true);
                break;
            case 5:
                this.checkGPStore.setChecked(true);
                break;
        }
    }

    @Override
    public void toogleProgressBar() {
        if (progressBar.getVisibility() == View.INVISIBLE)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setProgressText(String text) {
        this.textViewProgressBar.setText(text);
    }

    @Override
    public void showUserCancelledInstallationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.proceed();
            }
        });

        builder.setNegativeButton("ABORT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish(); // user cancelled the dialog
            }
        });

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("In order to make Google Apps work on your device you have to install " +
                "all the apps.");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(232, 106, 37));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton("GIVE NOW", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Uri packageURI = Uri.parse("package:" + Constants.GOOGLE_PLAY_SERVICES_PACKAGE_NAME);
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", packageURI);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("GIVE LATER", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // done
            }
        });

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("In order for common apps to work at full potential, you may have to give " +
                "Google Play services some permissions.\n" +
                "You can always give the required permissions later by browsing your settings.");

        builder.setIcon(R.drawable.aptoide_icon);

        // Create the AlertDialog
        final AlertDialog dialog = builder.create();

        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(232, 106, 37));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.rgb(232, 106, 37));
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
