package com.example.mvpexample.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.example.mvpexample.R;
import com.example.mvpexample.view.MainActivity;

public class FragmentNavigator {
    private final MainActivity activity;

    public FragmentNavigator(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    public void navigateTo(Fragment newFrag, boolean popBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFrag);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        if (popBackStack) //pop
            activity.getSupportFragmentManager().popBackStack();
        else
            transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
