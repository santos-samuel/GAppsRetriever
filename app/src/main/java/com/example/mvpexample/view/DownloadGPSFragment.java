package com.example.mvpexample.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.mvpexample.R;
import com.example.mvpexample.presenter.DownloadGPSPresenter;
import com.example.mvpexample.presenter.IDownloadGPSView;

public class DownloadGPSFragment extends Fragment implements IDownloadGPSView {
    private DownloadGPSPresenter presenter;
    private Button btnDownloadGPS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_gps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new DownloadGPSPresenter(this, ((MainActivity) getActivity()).getRequestManager());
        btnDownloadGPS = view.findViewById(R.id.btnDownloadGPS);

        View.OnClickListener downloadGPSListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.downloadGPSRequest();
            }
        };
        btnDownloadGPS.setOnClickListener(downloadGPSListener);
    }
}
