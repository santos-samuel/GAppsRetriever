package com.example.mvpexample.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvpexample.R;
import com.example.mvpexample.presenter.ISelectAndInspectApkView;
import com.example.mvpexample.presenter.SelectAndInspectApkPresenter;
import static android.app.Activity.RESULT_OK;

public class SelectAndInspectApkFragment extends Fragment implements ISelectAndInspectApkView {

    private static final int PICKAPK_RESULT_CODE = 100;

    SelectAndInspectApkPresenter presenter;
    private Button btnSearchFile;
    private TextView txtViewSelectedFile;
    private Button btnInspectApkAndNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selectapk_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearchFile = view.findViewById(R.id.btnSearchFile);
        txtViewSelectedFile = view.findViewById(R.id.txtViewSelectedFile);
        btnInspectApkAndNext = view.findViewById(R.id.btnInspectApkAndNext);

        presenter = new SelectAndInspectApkPresenter(this, ((MainActivity) getActivity()).getRequestManager(), ((MainActivity) getActivity()).getFragNavigator());

        View.OnClickListener searchFileListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.android.package-archive"); // select apk extension files only
                try {
                    startActivityForResult(intent, PICKAPK_RESULT_CODE);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(getActivity(), "There are no file explorer clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        };
        btnSearchFile.setOnClickListener(searchFileListener);


        View.OnClickListener inspectApkAndNextListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.inspectApk();
                presenter.changeFragment(new CheckGooglePlayServicesFragment());
            }
        };
        btnInspectApkAndNext.setOnClickListener(inspectApkAndNextListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKAPK_RESULT_CODE && resultCode == RESULT_OK && null != data) {
            presenter.notifySelectedFile(data);
        }
    }

    @Override
    public void updateSelectedFile(String newFileName) {
        txtViewSelectedFile.setText(newFileName);
        btnInspectApkAndNext.setEnabled(true);
    }
}