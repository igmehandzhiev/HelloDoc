package com.p4mi.igm.hellodoc;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SIGN_IN_ACTIVITY";
    GoogleSignInOptions gso;
    private SignInButton SignInBtn;
    private ImageView Logo, PatientType, DoctorType;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private FirebaseAuth firebaseAuth;
    private String userType;
    private Context context = this;
    private DatabaseReference myRef;
    private FirebaseDatabase database;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        SignInBtn = (SignInButton) findViewById(R.id.sign_in_button);
        Logo = (ImageView) findViewById(R.id.logo);
        PatientType = (ImageView) findViewById(R.id.type_patient);
        DoctorType = (ImageView) findViewById(R.id.type_doctor);
        SignInBtn.setOnClickListener(this);
        Logo.setOnClickListener(this);
        PatientType.setOnClickListener(this);
        DoctorType.setOnClickListener(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        firebaseAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.logo:
                final Context context = this;
                Intent intent = new Intent(context, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.type_doctor:
                PatientType.setBackgroundColor(0x000000);
                DoctorType.setBackgroundResource(R.color.wallet_highlighted_text_holo_light);
                typeDoctor();
                break;

            case R.id.type_patient:
                DoctorType.setBackgroundColor(0x000000);
                PatientType.setBackgroundResource(R.color.wallet_highlighted_text_holo_light);
                typePatient();
                break;
        }
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                FirebaseAuth.getInstance().signOut();
                createUser(FirebaseAuth.getInstance().getCurrentUser());
            }
        });
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                if (userType.equals("doctor")) {
                    Intent intent = new Intent(context, DoctorInfo.class);
                    startActivity(intent);
                } else if (userType.equals("patient")) {
                    Intent intent = new Intent(context, MainMenu.class);
                    startActivity(intent);
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            createUser(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.", Toast.LENGTH_SHORT)
                                    .show();
                            createUser(null);
                        }
                    }
                });
    }


    private void createUser(FirebaseUser user) {
        if (user != null) {
            // Name, email address
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference();
            writeNewUser(myRef, user.getDisplayName(), user.getEmail(), userType, user.getUid());

        }
    }

    private void writeNewUser(DatabaseReference databaseReference,
                              String displayName, String email, String type, String userID) {
        Account account = new Account(displayName, email, type, userID);
        databaseReference.child("users").child(type).child(userID).setValue(account);
    }

    protected void typeDoctor() {
        userType = "doctor";
    }

    protected void typePatient() {
        userType = "patient";
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
