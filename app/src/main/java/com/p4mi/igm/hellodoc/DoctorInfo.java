package com.p4mi.igm.hellodoc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class DoctorInfo extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private TextView doctorName;
    private Button updateInfo;
    private FirebaseUser user;
    private Account account;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        doctorName = (TextView) findViewById(R.id.doctor_name);
        doctorName.append(user.getDisplayName());
        updateInfo = (Button) findViewById(R.id.update_info);
        updateInfo.setOnClickListener(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        ValueEventListener updateInfoListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("INFO", "OnDataChange");
                account = dataSnapshot.child("users").child("doctor").child(user.getUid()).getValue(Account.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(updateInfoListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_info:
                updateInfo();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void updateInfo() {
        Log.d("INFO", myRef.toString());
        final EditText doctorPhoneNumber = (EditText) findViewById(R.id.doctor_phone);
        account.setPhone(doctorPhoneNumber.getText().toString());

        final Spinner doctorCity = (Spinner) findViewById(R.id.doctor_city);
        account.setCity(doctorCity.getSelectedItem().toString());

        final Spinner doctorType = (Spinner) findViewById(R.id.doctor_type);
        account.setDoctorType(doctorType.getSelectedItem().toString());

        final Spinner doctorHospital = (Spinner) findViewById(R.id.doctor_hospital);
        account.setHospitalType(doctorHospital.getSelectedItem().toString());

        final EditText hospitalName = (EditText) findViewById(R.id.hospital_name);
        account.setHospitalName(hospitalName.getText().toString());

        final EditText description = (EditText) findViewById(R.id.description);
        account.setDescription(description.getText().toString());

        myRef.child("users").child("doctor").child(user.getUid()).setValue(account);
        Intent intent = new Intent(this, Calendar.class);
        startActivity(intent);
    }
}
