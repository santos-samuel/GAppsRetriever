package com.example.mvpexample.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.mvpexample.R;

public class NavigatorClass {

    /*public static void navigateTo(FragmentActivity activity, Fragment newFrag) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFrag);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }*/

    public static void navigateTo(FragmentActivity activity, Fragment newFrag, boolean popBackStack) {
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
