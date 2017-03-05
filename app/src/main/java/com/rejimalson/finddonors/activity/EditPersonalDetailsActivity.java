package com.rejimalson.finddonors.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;
import com.rejimalson.finddonors.helper.DatePickerFragment;

public class EditPersonalDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText et_FullName;
    TextView tv_BirthDay;
    Spinner spinner_BloodGroup, spinner_Gender;
    FloatingActionButton fab_Save;

    RelativeLayout rl_BirthdayLayout;
    CoordinatorLayout mainLayout;

    String mFullName, mBloodGroup, mGender, mBirthday;

    ProgressDialog updateProgress;

    //Firebase Instance Here
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;
    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        //Get the intent data here
        mFullName = getIntent().getStringExtra("fullName");
        mBloodGroup = getIntent().getStringExtra("bloodGroup");
        mGender = getIntent().getStringExtra("gender");
        mBirthday = getIntent().getStringExtra("birthDay");

        updateProgress = new ProgressDialog(this);
        updateProgress.setMessage("Updating personal details....");
        updateProgress.setCancelable(false);

        //Set activity title here
        this.setTitle("Personal Details");

        //Set navigation up icon here
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Find view id here
        et_FullName = (EditText) findViewById(R.id.full_name_input);
        spinner_BloodGroup = (Spinner)findViewById(R.id.blood_grp_spinner);
        spinner_Gender = (Spinner)findViewById(R.id.gender_spinner);
        tv_BirthDay = (TextView)findViewById(R.id.select_birthday);
        fab_Save = (FloatingActionButton)findViewById(R.id.fab_save);
        rl_BirthdayLayout = (RelativeLayout)findViewById(R.id.bday_layout);
        mainLayout = (CoordinatorLayout)findViewById(R.id.activity_edit_personal_details);

        et_FullName.setText(mFullName);
        if (mBirthday.equals("Not Specified")) {
            tv_BirthDay.setText(R.string.select_birthday);
        } else {
            tv_BirthDay.setText(mBirthday);
        }

        //Create adapter for spinners here
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>
                (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, AppConfig.BLOOD_GROUP_LIST);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_BloodGroup.setAdapter(bloodGroupAdapter);

        if (mBloodGroup.equals("Not Specified")){
            spinner_BloodGroup.setSelection(bloodGroupAdapter.getPosition("Select"));
        } else {
            spinner_BloodGroup.setSelection(bloodGroupAdapter.getPosition(mBloodGroup));
        }

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>
                (getApplicationContext(), android.R.layout.simple_dropdown_item_1line, AppConfig.GENDER_LIST);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_Gender.setAdapter(genderAdapter);

        if (mGender.equals("Not Specified")){
            spinner_Gender.setSelection(genderAdapter.getPosition("Select"));
        } else {
            spinner_Gender.setSelection(genderAdapter.getPosition(mGender));
        }

        spinner_BloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBloodGroup = AppConfig.BLOOD_GROUP_LIST[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mGender = AppConfig.GENDER_LIST[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //On click Listener here
        rl_BirthdayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthDayCalender();
            }
        });

        fab_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePersonalDetails()){
                    updateProgress.show();
                    updatePersonalDetails(mFullName, mBloodGroup, mGender, mBirthday);
                }
            }
        });
    }

    private void updatePersonalDetails(final String mFullName, final String mBloodGroup,
                                       final String mGender, final String mBirthday) {
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mUserId = mUser.getUid();
            mDatabaseRef = mDatabase.getReference().child("Users").child(mUserId).child("Personal Details");
            mDatabaseRef.child("fullName").setValue(mFullName);
            mDatabaseRef.child("bloodGroup").setValue(mBloodGroup);
            mDatabaseRef.child("gender").setValue(mGender);
            mDatabaseRef.child("birthDay").setValue(mBirthday);
            Thread thread = new Thread(){

                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(2000);
                        updateProgress.dismiss();
                        Intent intent = new Intent(EditPersonalDetailsActivity.this,UserPageActivity.class);
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

    private boolean validatePersonalDetails() {
        if (AppConfig.validateName(et_FullName.getText().toString().trim())){
            mFullName = et_FullName.getText().toString().trim();
            if (mBloodGroup.equals("Select")){
                mBloodGroup = "Not Specified";
            }
            if (mGender.equals("Select")) {
                mGender = "Not Specified";
            }
            mBirthday = tv_BirthDay.getText().toString();
            if (mBirthday.equals("Select")){
                mBirthday = "Not Specified";
            }
            return true;
        } else {
            Snackbar.make(mainLayout, "Enter a valid name", Snackbar.LENGTH_SHORT).show();
            return false;
        }
    }

    private void showBirthDayCalender() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String bday,bmonth = null,byear;
        if(dayOfMonth<10)
        {
            bday = "0"+dayOfMonth;
        }
        else
        {
            bday = String.valueOf(dayOfMonth);
        }
        switch (month+1)
        {
            case 1:
                bmonth = "January";
                break;
            case 2:
                bmonth = "February";
                break;
            case 3:
                bmonth = "March";
                break;
            case 4:
                bmonth = "April";
                break;
            case 5:
                bmonth = "May";
                break;
            case 6:
                bmonth = "June";
                break;
            case 7:
                bmonth = "July";
                break;
            case 8:
                bmonth = "August";
                break;
            case 9:
                bmonth = "September";
                break;
            case 10:
                bmonth = "October";
                break;
            case 11:
                bmonth = "November";
                break;
            case 12:
                bmonth = "December";
                break;
        }
        byear = String.valueOf(year);
        String b_date = bmonth+" "+bday+","+" "+byear;
        tv_BirthDay.setText(b_date);
    }
}
