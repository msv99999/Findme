package com.findme.msv.findme;

import android.app.Service;
import android.location.LocationListener;

/**
 * Created by msv on 29/1/17.
 */

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;


    boolean serviceStopped;

    private Handler mHandler;
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("Run", "Run");

            if (serviceStopped == false)
            {
                //createNotificationIcon();
                getLocation();
                if (canGetLocation())
                {

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();


                }
                else {



                    //Toast.makeText(getBaseContext(),"GPS NA",Toast.LENGTH_SHORT).show();
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Log.d("NOGPS", "NOGPS");
                    showSettingsAlert();

                }
            }
            queueRunnable();
        }
    };

    private void queueRunnable() {
        mHandler.postDelayed(updateRunnable, 10*1000);

    }

    public GPSTracker(Context context) {
        this.mContext = context;
        Log.d("constructor2","constructor2");
    }

    public GPSTracker()
    {
        mContext=this;
        Log.d("constructor","constructor");
    }




    @Override
    public void onCreate() {
        //Toast.makeText(getBaseContext(), "check", Toast.LENGTH_SHORT).show();

        super.onCreate();
        //Toast.makeText(getBaseContext(), "oncreate()", Toast.LENGTH_SHORT).show();
        serviceStopped = false;
        mHandler = new Handler();
        Log.d("Oncreate", "Oncreate");
        /*
        getLocation();
        while(canGetLocation!=true)
        {
            showSettingsAlert();
            getLocation();
        }
        */

        queueRunnable();

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId) {
        //Toast.makeText(getBaseContext(), "onstart()", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        serviceStopped=true;
        super.onDestroy();
        Toast.makeText(getBaseContext(),"ondestroy()",Toast.LENGTH_SHORT).show();
    }




    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    /*
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */

    public void showSettingsAlert() {
        Log.d("SETTINGS1", "SETTINGS1");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        Log.d("SETTINGS2", "SETTINGS2");
        // Setting Dialog Title
        alertDialog.setTitle("GPS in settings");
        Log.d("SETTINGS3", "SETTINGS3");
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        Log.d("SETTINGS4", "SETTINGS4");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        Log.d("SETTINGS5", "SETTINGS5");
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        Log.d("SETTINGS6", "SETTINGS6");
        // Showing Alert Message
        alertDialog.show();
        Log.d("SETTINGS7", "SETTINGS7");
    }




    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}