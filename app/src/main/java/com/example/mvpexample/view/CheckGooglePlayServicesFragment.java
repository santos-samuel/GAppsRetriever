package com.example.mvpexample.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
}
