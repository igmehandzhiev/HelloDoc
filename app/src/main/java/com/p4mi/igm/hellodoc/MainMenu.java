package com.p4mi.igm.hellodoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.quickstart.NearMe;
import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private ImageView nearMe, schedule, doctorSearch, advancedSearch;
    private static Hashtable<String, Account> doctors = new Hashtable<>();
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        nearMe = (ImageView) findViewById(R.id.nearMe);
        schedule = (ImageView) findViewById(R.id.schedule);
        doctorSearch = (ImageView) findViewById(R.id.doctorSearch);
        advancedSearch = (ImageView) findViewById(R.id.advancedSearch);
        nearMe.setOnClickListener(this);
        schedule.setOnClickListener(this);
        doctorSearch.setOnClickListener(this);
        advancedSearch.setOnClickListener(this);
        getDoctors();
    }

    private void getDoctors() {
        HelloWindow.getMyRef().child("users").child("doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                doctors.clear();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    String name = item.child("name").getValue().toString();
                    Account account = item.getValue(Account.class);
                    doctors.put(name, account);

                }
                HelloWindow.getMyRef().child("users").child("doctor").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.nearMe:
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.schedule:
                intent = new Intent(this, UnderConstruction.class);
                startActivity(intent);
                break;
            case R.id.doctorSearch:
                intent = new Intent(this, DoctorSearch.class);
                startActivity(intent);
                break;
            case R.id.advancedSearch:
                intent = new Intent(this, UnderConstruction.class);
                startActivity(intent);
                break;
        }
    }

    public static Hashtable<String, Account> getDoctorsList() {
        return doctors;
    }
}
