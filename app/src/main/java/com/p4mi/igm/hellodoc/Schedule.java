package com.p4mi.igm.hellodoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.attr.key;

/**
 * Created by IGM on 16.6.2017 г..
 */

public class Schedule extends AppCompatActivity implements View.OnClickListener {
    private WorkingHours workingHours;
    private EditText openingHours, closingHours;
    private TextView request;
    private Button setHours;
    private AppointmentsRequest appointmentsRequests;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private ValueEventListener workingHoursListener;
    private ArrayAdapter<String> arrayAdapter;
    private ListView lv;
    private Account account;
    private final String myID = SignIn.getUserID();
    private final int HIGHLIGHT_COLOR = 0x80ffb0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //TODO: add spinner instead of EditText
        workingHoursListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                account = dataSnapshot.child("users").child("doctor").child(myID).getValue(Account.class);
                workingHours = account.getWorkingHours();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.addValueEventListener(workingHoursListener);
        openingHours = (EditText) findViewById(R.id.opening_hours);
        closingHours = (EditText) findViewById(R.id.closing_hours);
        setHours = (Button) findViewById(R.id.set_working_hours_btn);
        setHours.setOnClickListener(this);

        lv = (ListView) findViewById(R.id.requestList);
        getRequests();
        requestOnClickAccept();
        requestOnHoldDecline();

    }

    private void requestOnClickAccept() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = (ViewGroup) view;
                TextView request = (TextView) viewGroup.findViewById(R.id.txt);
                String[] requestSplit = request.getText().toString().split("( : )|( - )");
                String day = requestSplit[0];
                String startTime = requestSplit[1];
                String finishTime = requestSplit[2];
                String username = requestSplit[3];
                request.setHighlightColor(HIGHLIGHT_COLOR);
                updateSchedule(day, startTime, username);
            }
        });
    }

    private void updateSchedule(String day, String index, String username) {
        switch (day) {
            case "MONDAY":
                workingHours.getMonday()
                        .set(Integer.parseInt(index), username);
                break;
            case "TUESDAY":
                workingHours.getTuesday()
                        .set(Integer.parseInt(index), username);
                break;
            case "WEDNESDAY":
                workingHours.getWednesday()
                        .set(Integer.parseInt(index), username);
                break;
            case "THURSDAY":
                workingHours.getThursday()
                        .set(Integer.parseInt(index), username);
                break;
            case "FRIDAY":
                workingHours.getFriday()
                        .set(Integer.parseInt(index), username);
                break;
            case "SATURDAY":
                workingHours.getSaturday()
                        .set(Integer.parseInt(index), username);
                break;
            case "SUNDAY":
                workingHours.getSunday()
                        .set(Integer.parseInt(index), username);
                break;
        }
        myRef.child("users").child("doctor").child(myID).setValue(account);
    }

    private void requestOnHoldDecline() {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = (ViewGroup) view;
                TextView request = (TextView) viewGroup.findViewById(R.id.txt);
                String[] requestSplit = request.getText().toString().split("( : )|( - )");
                String day = requestSplit[0];
                String startTime = requestSplit[1];
                String finishTime = requestSplit[2];
                String username = requestSplit[3];
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/users/" + "/doctor/" + myID + "/workingHours/" + day.toLowerCase() + "/" + startTime, (startTime + " - " + finishTime));
                myRef.updateChildren(childUpdates);
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = (ViewGroup) view;
                TextView request = (TextView) viewGroup.findViewById(R.id.txt);
                String[] requestSplit = request.getText().toString().split("( : )|( - )");
                String day = requestSplit[0];
                String startTime = requestSplit[1];
                String finishTime = requestSplit[2];
                String username = requestSplit[3];
                request.setHighlightColor(HIGHLIGHT_COLOR);
                updateSchedule(day, startTime, username);
            }
        });
    }


    private void getRequests() {

        final ArrayList<String> requestList = new ArrayList<>();
        myRef.child("appointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> requests = dataSnapshot.getChildren().iterator();
                requestList.clear();
                while (requests.hasNext()) {
                    DataSnapshot request = requests.next();
                    StringBuilder sb = new StringBuilder();
                    String startTime = request.child("startTime").getValue().toString();
                    String finishTime = request.child("finishTime").getValue().toString();
                    String username = request.child("username").getValue().toString();
                    String day = request.child("day").getValue().toString();
                    sb.append(day.toUpperCase()).append(" : ").append(startTime).append(" - ").append(finishTime).append(" : ").append(username);
                    requestList.add(sb.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.simple_list_item_1,
                R.id.txt,
                requestList);
        lv.setAdapter(arrayAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_working_hours_btn:
                workingHours.setOPENING_TIME(Double.parseDouble(openingHours.getText().toString()));
                workingHours.setCLOSING_TIME(Double.parseDouble(closingHours.getText().toString()));
                myRef.child("users").child("doctor").child(myID).child("workingHours").setValue(workingHours);
                Toast.makeText(this, "Successfully set working hours", Toast.LENGTH_SHORT);
                break;
        }
    }
}
