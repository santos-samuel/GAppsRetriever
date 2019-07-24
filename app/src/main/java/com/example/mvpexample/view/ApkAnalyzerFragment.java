package com.example.mvpexample.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.mvpexample.R;
import com.example.mvpexample.presenter.ApkAnalyzerPresenter;
import com.example.mvpexample.presenter.IApkAnalyzerView;

import static android.app.Activity.RESULT_OK;

public class ApkAnalyzerFragment extends Fragment implements IApkAnalyzerView {

    private static final int PICKAPK_RESULT_CODE = 1;

    ApkAnalyzerPresenter presenter;
    private Button btnSearchFile;
    private TextView txtViewSelectedFile;
    private Button btnInspectApk;
    private LinearLayout linearLayoutSelectedFile;
    private TextView txtIfAppNeedsGPS;
    private LinearLayout layoutIfAppNeedsGPS;
    private Button btnCheckGPSAvailability;
    private Button btnDownloadGooglePlayServices;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.apkanalyzer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ApkAnalyzerPresenter(this, ((MainActivity) getActivity()).getRequestManager());

        btnSearchFile = view.findViewById(R.id.btnSearchFile);
        txtViewSelectedFile = view.findViewById(R.id.txtViewSelectedFile);
        btnInspectApk = view.findViewById(R.id.btnInspectApk);
        linearLayoutSelectedFile = view.findViewById(R.id.linearLayoutSelectedFile);
        txtIfAppNeedsGPS = view.findViewById(R.id.txtIfAppNeedsGPS);
        layoutIfAppNeedsGPS = view.findViewById(R.id.layoutIfAppNeedsGPS);
        btnCheckGPSAvailability = view.findViewById(R.id.btnCheckGPSAvailability);
        btnDownloadGooglePlayServices = view.findViewById(R.id.btnDownloadGooglePlayServices);

        View.OnClickListener searchFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.android.package-archive");
                //intent.setType("*/*");
                startActivityForResult(intent,PICKAPK_RESULT_CODE);
            }
        };
        btnSearchFile.setOnClickListener(searchFileListener);


        View.OnClickListener inspectApkListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.inspectApk();
            }
        };
        btnInspectApk.setOnClickListener(inspectApkListener);

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
                presenter.downloadGPSRequest();
            }
        };
        btnDownloadGooglePlayServices.setOnClickListener(downloadGPSListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("TAG", "FILE WAS SELECTED");
        Log.d("RESULT = ", "" + requestCode+ " " + resultCode);
        if (requestCode == PICKAPK_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            presenter.processSelectedFile(data);
        }
    }

    @Override
    public void updateSelectedFile(String newFileName) {
        txtViewSelectedFile.setText(newFileName);
        linearLayoutSelectedFile.setVisibility(View.VISIBLE);
    }

    @Override
    public void showIfAppNeedsGooglePlayServices(String info, boolean bool) {
        txtIfAppNeedsGPS.setText(info);
        layoutIfAppNeedsGPS.setVisibility(View.VISIBLE);
    }
}