package com.p4mi.igm.hellodoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class DoctorSearch extends AppCompatActivity implements View.OnClickListener {

    private static AutoCompleteTextView autocomplete;
    private Button chooseDoctor;
    private Hashtable<String, Account> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_search);
        autocomplete = (AutoCompleteTextView) findViewById(R.id.doctor_name_autocomplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, android.R.layout.select_dialog_item, putDoctorsInArray());
        autocomplete.setThreshold(2);
        autocomplete.setAdapter(adapter);

        chooseDoctor = (Button) findViewById(R.id.doctor_search_choose);
        chooseDoctor.setOnClickListener(this);

    }

    private ArrayList<String> putDoctorsInArray() {
        //useful for autocomplete
        doctors = MainMenu.getDoctorsList();
        ArrayList<String> arr = new ArrayList<>(doctors.keySet());
        return arr;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.doctor_search_choose:
                intent = new Intent(this, Calendar.class);
                startActivity(intent);
                break;

        }
    }

    public static String getDoctor() {
        return autocomplete.getText().toString();
    }
}
