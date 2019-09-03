package com.example.mvpexample.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mvpexample.R;
import com.example.mvpexample.model.Constants;
import com.example.mvpexample.presenter.DownloadFromServicePresenter;
import com.example.mvpexample.presenter.IDownloadFromServiceView;

public class DownloadFromServiceFragment extends Fragment implements IDownloadFromServiceView {

    private DownloadFromServicePresenter presenter;
    private String installedPackageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        installedPackageName = bundle.getString("installedPackageName");
        return inflater.inflate(R.layout.download_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DownloadFromServicePresenter(this, ((MainActivity) getActivity()).getRequestManager(), ((MainActivity) getActivity()).getFragNavigator());

        presenter.processInstalledApp(installedPackageName);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void showToastMessage(String m) {
        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAskUserIfHeWantsToDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.changeFragment(new DownloadGPSFragment());
            }
        });

        builder.setNegativeButton(R.string.cast_tracks_chooser_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishActivity(); // user cancelled the dialog
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

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showDeviceNotSupportedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                presenter.downloadGPSAndInstallPack(getActivity(), getContext().getPackageManager());
            }
        });

        builder.setNegativeButton(R.string.cast_tracks_chooser_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finishActivity(); // user cancelled the dialog
            }
        });

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage("The installed app contains features that may not " +
                "work without Google Play Services.\n" +
                "Do you want Aptoide to make Google Play Services " +
                "available on your device?");

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
    public void showGooglePlayServicesIsDisabledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent openGPSSettings = new Intent();
                openGPSSettings.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", Constants.GOOGLE_PLAY_SERVICES_PACKAGE_NAME, null);
                openGPSSettings.setData(uri);
                getActivity().startActivity(openGPSSettings);
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
                finishActivity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void showOkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                finishActivity();
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
