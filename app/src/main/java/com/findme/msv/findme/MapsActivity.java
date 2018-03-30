package com.findme.msv.findme;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;







import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.app.Activity;
import android.view.*;
import android.location.*;
import android.content.Context;
import android.widget.*;
import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    GPSTracker gps;

    double latitude;
    double longitude;

    Bundle b;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference ref1;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    String mail;


    String name,department,year,key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        ref1 = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        b=getIntent().getExtras();
        if(b!=null)
        {
            name=b.getString("name");
            department=b.getString("dep");
            year=b.getString("year");
            key=b.getString("key");
        }


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        name=name.replace(".","");
        DatabaseReference ref2=ref1.child("student");
        //Toast.makeText(getBaseContext(),department,Toast.LENGTH_SHORT).show();
        DatabaseReference ref3=ref2.child(department.toUpperCase());
        final DatabaseReference ref4=ref3.child(year);
        //Toast.makeText(getBaseContext(),ref4.getKey(),Toast.LENGTH_SHORT).show();
        ref4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Toast.makeText(getBaseContext(),snapshot.getKey().toString(),Toast.LENGTH_SHORT).show();
                    //emailid
                    String text=snapshot.getKey();
                    if(text.substring(0,text.lastIndexOf("@")-6+1).toLowerCase().equals(name.toLowerCase()))
                    {
                        DatabaseReference ref5=ref4.child(text);
                        DatabaseReference ref6=ref5.child("latitude");
                        DatabaseReference ref7=ref5.child("longitude");
                        ref6.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String l=dataSnapshot.getValue().toString();
                                Toast.makeText(getBaseContext(),l,Toast.LENGTH_SHORT).show();
                                latitude=Double.parseDouble(l);
                                //onMapReady(mMap);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        ref7.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String l=dataSnapshot.getValue().toString();
                                longitude=Double.parseDouble(l);
                                Toast.makeText(getBaseContext(),l,Toast.LENGTH_SHORT).show();
                                //onMapReady(mMap);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        /*



                        //Toast.makeText(getBaseContext(),"success  "+text.substring(0,text.lastIndexOf("@")-6+1),Toast.LENGTH_SHORT).show();
                        for(DataSnapshot snapshot2:snapshot.getChildren())
                        {
                            if(snapshot2.getKey().equals("latitude"))
                            {
                                latitude=snapshot2.getValue();
                            }
                            else
                            {
                                longitude=snapshot2.getValue();
                            }

                        }
                        */

                        break;
                    }

                    else
                    {
                        Toast.makeText(getBaseContext(),name.toLowerCase()+"Not found",Toast.LENGTH_SHORT).show();
                    }
                }
            }





            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    /*
        gps = new GPSTracker(MapsActivity.this);
        gps.getLocation();
        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    */
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.clear();








        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(latitude, longitude);
        Toast.makeText(getBaseContext(),latitude + " " + longitude, Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(loc).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

    }

}