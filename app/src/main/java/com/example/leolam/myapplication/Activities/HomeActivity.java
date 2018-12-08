package com.example.leolam.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.leolam.myapplication.Maps_Activity;
import com.example.leolam.myapplication.R;
import com.example.leolam.myapplication.SceneFormExample.LocationActivity;

//TODO: OnClickListener https://stackoverflow.com/questions/25905086/multiple-buttons-onclicklistener-android
public class HomeActivity extends AppCompatActivity {

    public static final int HOME_CAMERA_REQUEST = 100;
    public static final int REQUEST_IMAGE_CAPTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ImageView FreeRoamButton = findViewById(R.id.imageView4);
        ImageView EventsButton = findViewById(R.id.imageView7);
        ImageView ContactUsButton = findViewById(R.id.imageView8);
        ImageView MapButton = findViewById(R.id.imageView6);
        ImageView BuildingNavigationButton = findViewById(R.id.imageView2);


        //Changed to make merge easier.
        FreeRoamButton.setOnClickListener(view -> {
            //Intent signup = new Intent(HomeActivity.this, LocationActivity.class);
            Intent signup = new Intent(HomeActivity.this, AR_Page_Activity.class);
            startActivity(signup);
        });

        BuildingNavigationButton.setOnClickListener(view -> {
            Intent signup = new Intent(HomeActivity.this, BuildingListActivity.class);
            startActivity(signup);
        });

        EventsButton.setOnClickListener(view -> {
            Intent signup = new Intent(HomeActivity.this, EventsActivity.class);
            startActivity(signup);
        });

        ContactUsButton.setOnClickListener(view -> {
            Intent signup = new Intent(HomeActivity.this, ContactActivity.class);
            startActivity(signup);
        });

        MapButton.setOnClickListener(view -> {
            Intent signup = new Intent(HomeActivity.this, Maps_Activity.class);
            startActivity(signup);
        });
    }

    //TODO: Consider removing or moving to different class.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
            if (requestCode ==  HOME_CAMERA_REQUEST) {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Camera Request was denied. Cannot continue.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
    }
}
