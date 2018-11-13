package com.example.leolam.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import android.content.Intent;

import com.example.leolam.myapplication.Activities.ContactActivity;
import com.example.leolam.myapplication.Activities.EventsActivity;
import com.example.leolam.myapplication.Activities.HomeActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class map_main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private MapView mMapView;
    private GoogleMap mMap;
    private Location location;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String s){

    }

    public void onProviderDisabled(String s){

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //HomeActivity Button to HomeActivity page
                    Intent signup = new Intent(map_main.this, HomeActivity.class);
                    startActivity(signup);
        } else if (id == R.id.nav_roam) {

        } else if (id == R.id.nav_events) {
            //EventsActivity Button to EventsActivity Page
                    Intent signup = new Intent(map_main.this, EventsActivity.class);
                    startActivity(signup);

        } else if (id == R.id.nav_contact) {
            //ContactActivity Button to ContactActivity Page
                    Intent signup = new Intent(map_main.this, ContactActivity.class);
                    startActivity(signup);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSION_ACCESS_FINE_LOCATION );
        }
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        location = (Location) lm.getLastKnownLocation(provider);
        if(location.equals(null)){
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        LatLng gilgit = new LatLng(location.getLatitude(), location.getLongitude());
        //LatLng gilgit = new LatLng(35.307076, -80.735170); UNC Charlotte location
        mMap.addMarker(new MarkerOptions().position(gilgit).title(""));
        location = (Location) lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        gilgit = new LatLng(location.getLongitude(), location.getLatitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gilgit, 15.0f));
    }

}