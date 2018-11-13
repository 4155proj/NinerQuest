package com.example.leolam.myapplication.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.leolam.myapplication.R;
import com.example.leolam.myapplication.map_main;

public class HomeActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 55;
    static final int REQUEST_IMAGE_CAPTURE = 98;
    // THIS IS A TEST TO SEE IF MERGE WORKS OK!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView Free_Roam = findViewById(R.id.imageView4);
        ImageView Events = findViewById(R.id.imageView7);
        ImageView Contact = findViewById(R.id.imageView8);
        ImageView Map = findViewById(R.id.imageView6);
        ImageView Navigation = findViewById(R.id.imageView2);

        //Free Roam Button to AR Page
        Free_Roam.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View view) {

                //Intent signup = new Intent(MainActivity.this, AR_Activity.class);
                //startActivity(signup);

                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }

        });


        //Navigation Button to Building List
        Navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(HomeActivity.this, BuildingListActivity.class);
                startActivity(signup);
            }
        });

        //EventsActivity Button to EventsActivity Page
        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(HomeActivity.this, EventsActivity.class);
                startActivity(signup);
            }
        });

        //ContactActivity Button to ContactActivity Page
        Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(HomeActivity.this, ContactActivity.class);
                startActivity(signup);
            }
        });

        //Map Button to Map_main page
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(HomeActivity.this, map_main.class);
                startActivity(signup);
            }
        });
    }

    //Ask Permission to Use Camera
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                } else {

                    // permission denied

                }
                return;
            }

        }
    }
}
