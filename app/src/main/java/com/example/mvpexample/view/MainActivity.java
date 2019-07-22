package com.example.mvpexample.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mvpexample.R;
import com.example.mvpexample.model.FragmentNavigator;
import com.example.mvpexample.model.PersistentMemory;
import com.example.mvpexample.model.RequestManager;

public class MainActivity extends AppCompatActivity {

    private FragmentNavigator fragNavigator;
    private RequestManager requestManager;
    private PersistentMemory persistentMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.persistentMemory = new PersistentMemory();
        this.requestManager = new RequestManager(persistentMemory);
        this.fragNavigator = new FragmentNavigator(this); // init navigator with main activity
        this.fragNavigator.navigateTo(new ColorFragment(), true);
    }

    public FragmentNavigator getFragNavigator() {
        return fragNavigator;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }
}
