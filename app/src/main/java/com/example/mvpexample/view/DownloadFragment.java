package com.example.mvpexample.view;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.mvpexample.R;
import com.example.mvpexample.model.Constants;
import com.example.mvpexample.presenter.DownloadPresenter;
import com.example.mvpexample.presenter.IDownloadView;

public class DownloadFragment extends Fragment implements IDownloadView {

    private DownloadPresenter presenter;
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

        presenter = new DownloadPresenter(this, ((MainActivity) getActivity()).getRequestManager(), ((MainActivity) getActivity()).getFragNavigator());

        /*Log.d("INSTALLED APP", installedPackageName);

        if (installedPackageName.equals(Constants.GOOGLE_GPS_PACKAGE_NAME)) {
            //delete apk file?
            getActivity().finish();
        }

        else { // A new app has been installed
            try {
                boolean requiresGPS = requestManager.doesThisAppRequireGooglePlayServices(installedPackageName);

                if (requiresGPS) {
                    requestManager.checkIfGooglePlayServicesIsAvailable();
                }

                else {
                    Toast.makeText(this, "The installed/changed App doesn't need GPS", Toast.LENGTH_SHORT).show();
                    this.finish(); // close activity
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }*/
    }
}
