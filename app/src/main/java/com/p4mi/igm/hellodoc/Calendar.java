package com.p4mi.igm.hellodoc;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Calendar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner weekdays;
    private ListView lv;
    private ArrayAdapter<String> arrayAdapter;
    private String myID = SignIn.getUserID();
    private DatabaseReference myRef = HelloWindow.getMyRef();
    private Context context = this;
    private String day;


    public Calendar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);


        weekdays = (Spinner) findViewById(R.id.doctor_appointment_days);
        lv = (ListView) findViewById(R.id.list);

        weekdays.setOnItemSelectedListener(this);
        listViewOnClickRequestAppointment();
    }

    private void listViewOnClickRequestAppointment() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = (ViewGroup) view;
                TextView appointmentHour = (TextView) viewGroup.findViewById(R.id.txt);
                if (!appointmentHour.getText().toString().matches("\\w")) {
                    AppointmentsRequest appointmentsRequest = new AppointmentsRequest();
                    appointmentsRequest.setUsername(SignIn.getUsername());
                    String[] hour = appointmentHour.getText().toString().split(" ");
                    String startTime = hour[0];
                    String endTime = hour[2];
                    appointmentsRequest.setStartTime(startTime);
                    appointmentsRequest.setFinishTime(endTime);
                    appointmentsRequest.setDay(day);
                    myRef.child("appointments").child(myID).setValue(appointmentsRequest);
                    Toast.makeText(context, "Appointment Requested. Please wait for doctor's answer", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void listData(String day) {
        String doctor = DoctorSearch.getDoctor();
        WorkingHours workingHours = MainMenu.getDoctorsList().get(doctor).getWorkingHours();
        int open = (int) workingHours.getOPENING_TIME();
        int close = (int) workingHours.getCLOSING_TIME();
        ArrayList<String> selectedDay = new ArrayList<>();
        switch (day) {
            case "Monday":
                selectedDay.addAll(workingHours.getMonday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Tuesday":
                selectedDay.addAll(workingHours.getTuesday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Wednesday":
                selectedDay.addAll(workingHours.getWednesday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Thursday":
                selectedDay.addAll(workingHours.getThursday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Friday":
                selectedDay.addAll(workingHours.getFriday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Saturday":
                selectedDay.addAll(workingHours.getSaturday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
            case "Sunday":
                selectedDay.addAll(workingHours.getSunday().subList(open, close));
                arrayAdapter = new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item_1,
                        R.id.txt,
                        selectedDay);
                break;
        }
        lv.setAdapter(arrayAdapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        day = weekdays.getSelectedItem().toString();
        listData(day);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
