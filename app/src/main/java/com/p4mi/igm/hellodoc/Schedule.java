package com.p4mi.igm.hellodoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.calendar.*;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttachment;

import android.provider.CalendarContract;

import java.io.IOException;
import java.util.List;

/**
 * Created by IGM on 16.6.2017 г..
 */

public class Schedule extends AppCompatActivity {
    private GoogleApiClient mGoogleApiClient;
    private Calendar calendarService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me);

    }
}
