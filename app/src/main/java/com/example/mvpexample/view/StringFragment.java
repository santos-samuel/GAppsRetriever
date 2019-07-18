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
import com.example.mvpexample.model.NavigatorClass;
import com.example.mvpexample.presenter.IStringView;
import com.example.mvpexample.presenter.StringPresenter;

public class StringFragment extends Fragment implements IStringView {

    private StringPresenter presenter;
    private Button btnChangeString;
    private TextView textViewString;
    private Button btnChangeFrag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.string_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new StringPresenter(this);
        textViewString = view.findViewById(R.id.textViewString);
        btnChangeString = view.findViewById(R.id.btnChangeString);
        btnChangeFrag = view.findViewById(R.id.btnChangeFrag);

        View.OnClickListener changeStringListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "String button pressed");

                String oldString = textViewString.getText().toString();
                presenter.changeString(oldString);

            }
        };
        btnChangeString.setOnClickListener(changeStringListener);




        View.OnClickListener changeFragListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "Change Frag button pressed");

                NavigatorClass.navigateTo(getActivity(), new ColorFragment(), true); // ASK ASK ASK
            }
        };
        btnChangeFrag.setOnClickListener(changeFragListener);
    }

    @Override
    public void updateViewString(String string) {
        textViewString.setText(string);
    }
}
