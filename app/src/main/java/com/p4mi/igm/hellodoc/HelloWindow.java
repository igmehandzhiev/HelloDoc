package com.p4mi.igm.hellodoc;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HelloWindow extends AppCompatActivity {
    private static int TIME_OUT = 2000;
    private final String TAG = "Hello";
    private Context context = this;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ValueEventListener userSignedListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_window);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userSignedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (currentUser != null) {
                    Account account = null;
                    if (dataSnapshot.child("users").child("doctor").hasChild(currentUser.getUid())) {
                        account = dataSnapshot.child("users").child("doctor").child(currentUser.getUid()).getValue(Account.class);
                    } else if (dataSnapshot.child("users").child("patient").hasChild(currentUser.getUid())) {
                        account = dataSnapshot.child("users").child("patient").child(currentUser.getUid()).getValue(Account.class);
                    } else {
                        Toast.makeText(context, "Not such an user", Toast.LENGTH_SHORT);
                    }
                    if (account.getType().equals("doctor")) {
                        Intent intent = new Intent(context, Calendar.class);
                        startActivity(intent);
                    } else if (account.getType().equals("patient")) {
                        Intent intent = new Intent(context, MainMenu.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(context, SignIn.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.addAuthStateListener(mAuthListener);
        myRef.addValueEventListener(userSignedListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        firebaseAuth.removeAuthStateListener(mAuthListener);
    }
}
