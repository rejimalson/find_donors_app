package com.rejimalson.finddonors.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;

import java.util.ArrayList;
import java.util.Objects;

public class EditContactDetailsActivity extends AppCompatActivity {

    EditText et_PhoneNumber;
    Spinner spinner_State, spinner_District;
    FloatingActionButton fab_Save;

    RelativeLayout districtLayout;
    CoordinatorLayout mainLayout;

    String mPhoneNumber;
    String mState, mStateId, mDistrict;

    ArrayList<String> arrayList_States = new ArrayList<>();
    ArrayList<String> arrayList_Districts = new ArrayList<>();

    ProgressDialog loadDistrictsProgress, updateProgress;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact_details);

        mAuth = FirebaseAuth.getInstance();
        //Initialize Firebase database
        mDatabase = FirebaseDatabase.getInstance();

        //Remove +91 - from the phone number
        mPhoneNumber = getIntent().getStringExtra("phoneNumber").substring(6,16);
        mState = getIntent().getStringExtra("state");
        mDistrict = getIntent().getStringExtra("district");

        this.setTitle("Contact Details");

        //Set navigation up icon here
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        loadDistrictsProgress = new ProgressDialog(this);
        loadDistrictsProgress.setMessage("Loading Districts....");
        loadDistrictsProgress.setCancelable(false);

        updateProgress = new ProgressDialog(this);
        updateProgress.setMessage("Updating contact details....");
        updateProgress.setCancelable(false);

        //Find View Id here
        et_PhoneNumber = (EditText)findViewById(R.id.phone_number_input);
        spinner_State = (Spinner)findViewById(R.id.state_spinner);
        spinner_District = (Spinner)findViewById(R.id.district_spinner);
        fab_Save = (FloatingActionButton)findViewById(R.id.fab_save);
        districtLayout = (RelativeLayout)findViewById(R.id.district_spinner_layout);
        mainLayout = (CoordinatorLayout)findViewById(R.id.activity_edit_contact_details);

        //Assign intent data to the fields here
        et_PhoneNumber.setText(mPhoneNumber);

        populateStateSpinner();
        spinner_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = arrayList_States.get(position);
                mStateId = String.valueOf(position);
                if (!mState.equals("Select")){
                    loadDistrictsProgress.show();
                    populateDistrictSpinner(mStateId);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_District.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDistrict = arrayList_Districts.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fab_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateContactDetails()){
                    updateProgress.show();
                    updateContactDetails();
                }
            }
        });
    }

    private void updateContactDetails() {
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mUserId = mUser.getUid();
            mDatabaseRef = mDatabase.getReference().child("Users").child(mUserId).child("Contact Details");
            mDatabaseRef.child("phone").setValue(mPhoneNumber);
            mDatabaseRef.child("district").setValue(mDistrict);
            mDatabaseRef.child("state").setValue(mState);
            Thread thread = new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        updateProgress.dismiss();
                        Intent intent = new Intent(EditContactDetailsActivity.this,UserPageActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    private boolean validateContactDetails() {
        if (AppConfig.validatePhone(et_PhoneNumber.getText().toString().trim())){
            mPhoneNumber = et_PhoneNumber.getText().toString().trim();
                if (mState.equals("Select")){
                    mState = "Not Specified";
                }
                if (mDistrict.equals("Select")){
                    mDistrict = "Not Specified";
                }
                return true;
        }else {
            Snackbar.make(mainLayout, "Enter a valid phone number", Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    private void populateDistrictSpinner(String mStateId) {
        mDatabaseRef = mDatabase.getReference().child("Country").child("India").child("DistrictsOfIndia");
        Query query = mDatabaseRef.orderByChild("state_id").equalTo(mStateId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList_Districts.clear();
                arrayList_Districts.add("Select");
                for (DataSnapshot districtSnapshot : dataSnapshot.getChildren() ){
                    String district = (String)districtSnapshot.child("districtName").getValue();
                    arrayList_Districts.add(district);
                }
                ArrayAdapter<String> arrayAdapter_Districts = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_Districts);
                arrayAdapter_Districts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_District.setAdapter(arrayAdapter_Districts);
                loadDistrictsProgress.dismiss();
                if (mDistrict.equals("Not Specified")){
                    spinner_District.setSelection(arrayAdapter_Districts.getPosition("Select"));
                } else {
                    spinner_District.setSelection(arrayAdapter_Districts.getPosition(mDistrict));
                }
                districtLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText
                        (getApplicationContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateStateSpinner() {
        mDatabaseRef = mDatabase.getReference().child("Country").child("India").child("StatesOfIndia");
        Query query = mDatabaseRef.orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList_States.clear();
                arrayList_States.add("Select");
                for (DataSnapshot stateSnapshot : dataSnapshot.getChildren() ){
                    String state = (String)stateSnapshot.child("stateName").getValue();
                    arrayList_States.add(state);
                }
                ArrayAdapter<String> arrayAdapter_States = new ArrayAdapter<>
                        (getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList_States);
                arrayAdapter_States.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_State.setAdapter(arrayAdapter_States);
                if (mState.equals("Not Specified")){
                    spinner_State.setSelection(arrayAdapter_States.getPosition("Select"));
                } else {
                    spinner_State.setSelection(arrayAdapter_States.getPosition(mState));
                }
                mainLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText
                        (getApplicationContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showExitAlertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.epd_show_exit_message)
                .setTitle(R.string.epd_show_exit_title);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        showExitAlertMessage();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        showExitAlertMessage();
    }
}
