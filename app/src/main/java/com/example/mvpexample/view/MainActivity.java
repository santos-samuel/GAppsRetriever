package com.example.mvpexample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mvpexample.R;
import com.example.mvpexample.model.NavigatorClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavigatorClass.navigateTo(this, new ColorFragment(), true);
    }
}
