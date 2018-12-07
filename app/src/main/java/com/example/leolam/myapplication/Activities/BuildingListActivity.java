package com.example.leolam.myapplication.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.leolam.myapplication.Maps_Activity;
import com.example.leolam.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class BuildingListActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 55;
    static final int REQUEST_IMAGE_CAPTURE = 98;
    private double selectedLat = 0.0;
    private double selectedLong = 0.0;

    private ListView listview;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference getmDatabase = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_building__list);

        Button StartNavPage = (Button) findViewById(R.id.StartNavPage);

        StartNavPage.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View view) {

                ActivityCompat.requestPermissions(BuildingListActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);

                Intent signup = new Intent(BuildingListActivity.this, Maps_Activity.class);

                signup.putExtra("LATITUDE", selectedLat);
                signup.putExtra("LONGITUDE", selectedLong);
                startActivity(signup);
            }

        });

        getmDatabase.child("Building_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> BuildingList = new ArrayList<String>();
                final List<Double> Lat = new ArrayList<Double>();
                final List<Double> Long = new ArrayList<Double>();

                listview = (ListView) findViewById(R.id.listview);

                for (DataSnapshot addressSnapshot : dataSnapshot.getChildren()) {
                    String buildingName = addressSnapshot.child("name").getValue(String.class);
                    Double latitude = addressSnapshot.child("lat").getValue(Double.class);
                    Double longitude = addressSnapshot.child("lon").getValue(Double.class);


                    if (buildingName != null) {
                        BuildingList.add(buildingName);
                    }

                    if (latitude != null) {
                        Lat.add(latitude);
                    }

                    if (longitude != null) {
                        Long.add(longitude);
                    }

                }
                // Create an ArrayAdapter using the string array and a default spinner layout
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(BuildingListActivity.this, android.R.layout.simple_spinner_item, BuildingList);
                final ArrayAdapter<Double> addressAdapter2 = new ArrayAdapter<Double>(BuildingListActivity.this, android.R.layout.simple_list_item_1, Lat);
                final ArrayAdapter<Double> addressAdapter3 = new ArrayAdapter<Double>(BuildingListActivity.this, android.R.layout.simple_list_item_2, Long);

                listview.setAdapter(addressAdapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        view.setSelected(true);
                        String selectedFromList = (String)(listview.getItemAtPosition(position));

                         selectedLat = (Double) (addressAdapter2.getItem(position));
                         selectedLong = (Double) (addressAdapter3.getItem(position));

                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

                }
                else {

                    // permission denied

                }
                return;
            }

        }
    }

}

