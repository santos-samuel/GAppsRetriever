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
    private Button btnDownloadGPSMarket;
    private Button btnDownloadGPSDirect;
    private Button btnToChangeNameLater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_gps_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDownloadGPSMarket = view.findViewById(R.id.btnDownloadGPSMarket);
        btnDownloadGPSDirect = view.findViewById(R.id.btnDownloadGPSDirect);
        btnToChangeNameLater = view.findViewById(R.id.btnToChangeNameLater);

        presenter = new DownloadGPSPresenter(this, ((MainActivity) getActivity()).getRequestManager());

        View.OnClickListener downloadGPSMarketListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.downloadGPSRequestMarket();
            }
        };
        btnDownloadGPSMarket.setOnClickListener(downloadGPSMarketListener);

        View.OnClickListener downloadGPSMDirectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.downloadGPSRequestDirect();
            }
        };
        btnDownloadGPSDirect.setOnClickListener(downloadGPSMDirectListener);

        View.OnClickListener btnToChangeNameLaterListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkHardwareInfo();
            }
        };
        btnToChangeNameLater.setOnClickListener(btnToChangeNameLaterListener);
    }
}
