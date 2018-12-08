package com.example.leolam.myapplication.Activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.leolam.myapplication.Maps_Activity;
import com.example.leolam.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Button Create_events = (Button)findViewById(R.id.create_event);
        Button Go_to_event = (Button)findViewById(R.id.create_event);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference getmDatabase = database.getReference();

        //ContactActivity Button to ContactActivity Page
        Create_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(EventsActivity.this, CreateEventsActivity.class);
                startActivity(signup);
            }
        });

    }
}
