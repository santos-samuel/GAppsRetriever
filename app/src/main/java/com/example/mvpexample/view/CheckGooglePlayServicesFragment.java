package com.example.mvpexample.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mvpexample.R;
import com.example.mvpexample.presenter.CheckGooglePlayServicesPresenter;
import com.example.mvpexample.presenter.ICheckGooglePlayServicesView;

public class CheckGooglePlayServicesFragment extends Fragment implements ICheckGooglePlayServicesView {

    private CheckGooglePlayServicesPresenter presenter;
    private TextView txtIfAppNeedsGPS;
    private Button btnCheckGPSAvailability;
    private Button btnChangeToDownloadFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.check_gps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtIfAppNeedsGPS = view.findViewById(R.id.txtIfAppNeedsGPS);
        btnCheckGPSAvailability = view.findViewById(R.id.btnCheckGPSAvailability);
        btnChangeToDownloadFragment = view.findViewById(R.id.btnChangeToDownloadFragment);

        presenter = new CheckGooglePlayServicesPresenter(this, ((MainActivity) getActivity()).getRequestManager(), ((MainActivity) getActivity()).getFragNavigator(), getArguments());

        View.OnClickListener checkGPSListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkGPSAvailability();
            }
        };
        btnCheckGPSAvailability.setOnClickListener(checkGPSListener);


        View.OnClickListener downloadGPSListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.changeFragment(new DownloadGPSFragment());
            }
        };
        btnChangeToDownloadFragment.setOnClickListener(downloadGPSListener);


    }
    @Override
    public void showIfSelectedAppNeedsGooglePlayServices(String info) {
        txtIfAppNeedsGPS.setText(info);
    }

    @Override
    public void showDialog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Add the buttons
        builder.setNeutralButton(R.string.cast_tracks_chooser_dialog_ok, null);

        // Set other dialog properties
        builder.setTitle("Alert");
        builder.setMessage(s);

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
}
