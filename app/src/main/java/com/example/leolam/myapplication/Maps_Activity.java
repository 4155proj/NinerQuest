package com.example.leolam.myapplication;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.leolam.myapplication.Activities.BuildingListActivity;
import com.example.leolam.myapplication.SceneFormExample.LocationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Build.*;

//TODO: Remove unneeded comments
public class Maps_Activity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 13;
    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();
    private Location location;
    Polyline line;
    double destLat;
    double destLong;
    String buildName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        destLat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        destLong = getIntent().getDoubleExtra("LONGITUDE", 0.0);
        buildName = getIntent().getStringExtra("BuildingName");

        Button navigateButton = (Button) findViewById(R.id.navigateButton);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View view) {

                ActivityCompat.requestPermissions(Maps_Activity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);

                Intent signup = new Intent(Maps_Activity.this, LocationActivity.class);
                signup.putExtra("LATITUDE", destLat);
                signup.putExtra("LONGITUDE", destLong);
                signup.putExtra("BuildingName", buildName);
                startActivity(signup);
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        location = (Location) lm.getLastKnownLocation(provider);
        if (location.equals(null)) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        double destLat = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double destLong = getIntent().getDoubleExtra("LONGITUDE", 0.0);
        String buildName = getIntent().getStringExtra("BuildingName");
        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());

        LatLng dest = new LatLng(destLat, destLong);
        if (markerPoints.size() > 1) {
            markerPoints.clear();
            mMap.clear();
        }
        markerPoints.add(origin);
        markerPoints.add(dest);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 15.0f));



        // Add new marker to the Google Map Android API V2
        mMap.addMarker(new MarkerOptions().position(origin).title("Starting"));
        mMap.addMarker(new MarkerOptions().position(dest).title("Ending"));

        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            origin = (LatLng) markerPoints.get(0);
            dest = (LatLng) markerPoints.get(1);

            // Getting URL to the Google Directions API=
            String url = getDirectionsUrl(origin, dest);
            //String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+origin,dest + "&key=" + MY_API_KEY;

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }




    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }


        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            points = new ArrayList();
            lineOptions = new PolylineOptions();
            for (int i = 0; i < result.size(); i++) {

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }



            }

// Drawing polyline in the Google Map for the i-th route
            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(Color.RED);
            lineOptions.geodesic(true);
            lineOptions.visible(true);
            mMap.addPolyline(lineOptions);
        }


    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=AIzaSyAkRBwjgQhb-BRyGsZuv91FTZcq0Dzd3DA";


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}

