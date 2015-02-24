package com.madchya;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by steven on 2015/2/19.
 */
public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        PreferencesActivity preferencesActivity = new PreferencesActivity();
        transaction.replace(android.R.id.content, preferencesActivity);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
