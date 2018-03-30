package com.findme.msv.findme;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
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
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5; // 5 minutes

    // Declaring a Location Manager
    protected LocationManager locationManager;

    boolean serviceStopped;

    private Handler mHandler;

    WifiManager wifi;
    List<ScanResult> results;
    int size = 0;
    String ITEM_KEY = "key";
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();




    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref1;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String mail;
    String dept,yearmap;
    Map<String, String> map;


    @Override
    public void onCreate() {


        super.onCreate();

        map = new HashMap<String, String>();
        map.put("17","1");
        map.put("16","2");
        map.put("15","3");
        map.put("14","4");


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ref1 = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mail=mUser.getEmail();
        dept=mail.substring(mail.lastIndexOf("@")+1).substring(0,mail.lastIndexOf(".")-9-mail.lastIndexOf("@"));
        yearmap=mail.substring(mail.lastIndexOf("@")-5).substring(0,2);
        //Log.d(yearmap,yearmap);
        // Toast.makeText(getBaseContext(),map.get("15"),Toast.LENGTH_SHORT).show();

        serviceStopped = false;
        mHandler = new Handler();
        Log.d("Oncreate", "Oncreate");

/*
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            //Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        registerReceiver(new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context c, Intent intent)
            {
                results = wifi.getScanResults();
                //Toast.makeText(getApplicationContext(), results.toString(), Toast.LENGTH_LONG).show();
                size = results.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

*/
        queueRunnable();

    }






    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {


            if (serviceStopped == false)
            {

            /*
                arraylist.clear();
                wifi.startScan();

                //Toast.makeText(getApplicationContext(), "Scanning...." + size, Toast.LENGTH_SHORT).show();
                try
                {
                    size = size - 1;
                    while (size >= 0)
                    {
                        HashMap<String, String> item = new HashMap<String, String>();
                        item.put(ITEM_KEY, results.get(size).SSID + "  " + results.get(size).capabilities);

                        arraylist.add(item);
                        //Toast.makeText(getApplicationContext(), results.get(size).SSID , Toast.LENGTH_SHORT).show();
                        size--;

                    }
                }
                catch (Exception e)
                {

                }

            */


                //createNotificationIcon();
                getLocation();
                if (canGetLocation())
                {

                    //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                      //      + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    //Toast.makeText(getBaseContext(),mail,Toast.LENGTH_SHORT).show();

                    /*
                    Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                    hopperUpdates.put(mail,latitude);
                    DatabaseReference ref2=ref1.child(mail);
                    */
                    //ref2.push().setValue(hopperUpdates);

                    if(mail.substring(mail.lastIndexOf('@')).matches("@ssn.edu.in"))
                    {
                        //Faculty
                        mail=mail.replace(".","_");
                        DatabaseReference ref2=ref1.child("faculty");

                    }
                    else
                    {
                        //Student
                        mail=mail.replace(".","_");
                        DatabaseReference ref2=ref1.child("student");
                        DatabaseReference ref3=ref2.child(dept.toUpperCase());

                        String year=map.get(yearmap);
                        //Toast.makeText(getBaseContext(),year,Toast.LENGTH_SHORT).show();
                        DatabaseReference ref4=ref3.child(year);
                        DatabaseReference ref5=ref4.child(mail);
                        DatabaseReference ref6=ref5.child("latitude");
                        DatabaseReference ref7=ref5.child("longitude");
                        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                        hopperUpdates.put("latitude", latitude);
                        ref5.updateChildren(hopperUpdates);
                        hopperUpdates.put("longitude", longitude);
                        ref5.updateChildren(hopperUpdates);

                        //Toast.makeText(getBaseContext(),latitude+"",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(),longitude+"",Toast.LENGTH_SHORT).show();
                    }
                }
                else {



                    //Toast.makeText(getBaseContext(),"GPS NA",Toast.LENGTH_SHORT).show();
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Log.d("NOGPS", "NOGPS");


                }
            }
            queueRunnable();
        }
    };

    private void queueRunnable() {
        mHandler.postDelayed(updateRunnable, 5*1000);

    }

    public GPSTracker(Context context) {
        this.mContext = context;

    }

    public GPSTracker()
    {
        mContext=this;

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

    public boolean canGetLocation() {
        return this.canGetLocation;
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
                Log.d("Show settings Alert","");
                //showSettingsAlert();
            } else {
                this.canGetLocation = true;

                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);


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





    public void showSettingsAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS in settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        //Log.d("SETTINGS4", "SETTINGS4");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

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