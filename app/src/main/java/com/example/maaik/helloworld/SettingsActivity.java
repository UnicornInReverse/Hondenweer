package com.example.maaik.helloworld;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends PreferenceActivity implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = "Settings";
    private static final String KEY_PREF_SYNC_CONN = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Settings");
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String syncConnPref = sharedPref.getString(SettingsActivity.KEY_PREF_SYNC_CONN, "");

        Log.d(LOG_TAG, "Pref: " + syncConnPref);

//        Button btn_back = (Button) findViewById(R.id.btn_back);
//        btn_back.setOnClickListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(KEY_PREF_SYNC_CONN)) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
