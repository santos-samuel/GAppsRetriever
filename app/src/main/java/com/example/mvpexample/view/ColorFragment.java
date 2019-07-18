package com.example.mvpexample.view;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mvpexample.R;
import com.example.mvpexample.model.NavigatorClass;
import com.example.mvpexample.presenter.ColorPresenter;
import com.example.mvpexample.presenter.IColorView;

public class ColorFragment extends Fragment implements IColorView {

    private ColorPresenter presenter;
    private Button btnChangeColor;
    private Button btnChangeFrag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.color_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("CREATED", "on view created");
        super.onViewCreated(view, savedInstanceState);

        presenter = new ColorPresenter(this);
        btnChangeColor = view.findViewById(R.id.btnChangeColor);
        btnChangeFrag = view.findViewById(R.id.btnChangeFrag);

        View.OnClickListener changeColorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "Color button pressed");
                int currentColor = -1;

                Drawable background = getView().getBackground();
                if (background instanceof ColorDrawable) {
                    currentColor = ((ColorDrawable) background).getColor();
                    Log.d("BACKGROUND", "background");
                }

                Log.d("COLOR OLD", String.valueOf(currentColor));
                presenter.changeColor(currentColor);
                Log.d("COLOR NEW", String.valueOf(currentColor));
            }
        };
        btnChangeColor.setOnClickListener(changeColorListener);



        View.OnClickListener changeFragListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "Change Frag button pressed");

                NavigatorClass.navigateTo(getActivity(), new StringFragment(), false); // ASK ASK ASK
            }
        };
        btnChangeFrag.setOnClickListener(changeFragListener);
    }

    @Override
    public void updateViewColor(int colorID) {
        getView().setBackgroundColor(colorID);
    }
}
