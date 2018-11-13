package com.example.leolam.myapplication.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.leolam.myapplication.R;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Button Create_events = (Button)findViewById(R.id.create_event);

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
