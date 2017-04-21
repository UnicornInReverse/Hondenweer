package com.example.maaik.helloworld;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class WelcomeScreenActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String LOG_TAG = "MapConnected";
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double longitude = 1.0;
    private double latitude = 1.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        Button btnScreen2 = (Button) findViewById(R.id.btn_screen2);
        btnScreen2.setOnClickListener(this);

        //Set up connection with GoogleAPI
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //Create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onStart() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean dog = preferences.getBoolean("dogs", true);
        Log.d(LOG_TAG, "Pref " + dog);

        View view = this.findViewById(android.R.id.content);

        if(dog) {
            view.setBackgroundResource(R.drawable.puppers);
        } else {
            view.setBackgroundResource(R.drawable.kitten);
        }

        //Connect with GoogleAPI
        mGoogleApiClient.connect();
        super.onStart();
        Log.d(LOG_TAG, "Connected with Google API");
    }

    protected void onStop() {
        //Disconnect with GoogleAPI
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View v) {
        //Determine onClick behaviour based on button id
        switch (v.getId()) {

            case R.id.btn_screen2:
                sendLocation();
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Check for permission
        createLocationRequest();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "No permission");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_LOCATION);

            return;

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //Get location
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        Log.d(LOG_TAG, "De locatie is veranderd " + location);
    }

    private void sendLocation() {
        //Send location to WeatherResultActivity
                Intent i = new Intent(this, WeatherResultActivity.class);
        i.putExtra("longitude", longitude);
        i.putExtra("latitude", latitude);
        startActivity(i);
    }
}
