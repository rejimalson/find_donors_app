package com.rejimalson.finddonors.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;
import com.rejimalson.finddonors.helper.EditTextErrorIcon;
import com.rejimalson.finddonors.helper.UserDetails;
import com.rejimalson.finddonors.helper.UserInfo;

public class SignUpActivity extends AppCompatActivity {

    //Declare instance here
    private TextInputLayout mNameLayout, mPhoneLayout, mEmailLayout,mPasswordLayout,mCPasswordLayout;
    private EditTextErrorIcon mName, mPhone, mEmail, mPassword, mCPassword;
    private Button mSignUp;
    private RelativeLayout mRelativeLayout;

    private Drawable successIcon, errorIcon;
    private ProgressDialog signUpProgress;
    private String mUserId;

    private String DEFAULT_DATA = "Not Specified";

    //Declare Firebase Instance here
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Set Title of the activity here
        this.setTitle(R.string.signUpActivityTitle);

        //Enable back button on action bar here
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Get the error and success icons here
        getValidationErrorIcon();
        getValidationSuccessIcon();

        //Initialize the Progress Dialog here
        signUpProgress = new ProgressDialog(this);
        signUpProgress.setMessage("Signing Up Please wait...");
        signUpProgress.setCancelable(false);

        //Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Find View Id here
        mNameLayout = (TextInputLayout)findViewById(R.id.su_name_input_layout);
        mPhoneLayout = (TextInputLayout)findViewById(R.id.su_phone_input_layout);
        mEmailLayout = (TextInputLayout)findViewById(R.id.su_email_input_layout);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.su_pwd_input_layout);
        mCPasswordLayout = (TextInputLayout)findViewById(R.id.su_cpwd_input_layout);

        mName = (EditTextErrorIcon)findViewById(R.id.su_input_name);
        mPhone = (EditTextErrorIcon)findViewById(R.id.su_input_phone);
        mEmail = (EditTextErrorIcon) findViewById(R.id.su_input_email);
        mPassword = (EditTextErrorIcon) findViewById(R.id.su_input_pwd);
        mCPassword = (EditTextErrorIcon) findViewById(R.id.su_input_cpwd);

        mSignUp = (Button)findViewById(R.id.sign_up_btn_id);

        mRelativeLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);

        //Set Text watcher here
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(AppConfig.validateName(mName.getText().toString().trim())){
                    mNameLayout.setErrorEnabled(false);
                    mName.setError(null,successIcon);
                }else {
                    mNameLayout.setError("Enter valid name");
                    mName.setError(null,errorIcon);
                }
            }
        });
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(AppConfig.validatePhone(mPhone.getText().toString().trim())){
                    mPhoneLayout.setErrorEnabled(false);
                    mPhone.setError(null,successIcon);
                }else {
                    mPhoneLayout.setError("Enter valid phone number");
                    mPhone.setError(null,errorIcon);
                }
            }
        });
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(AppConfig.validateEmail(mEmail.getText().toString().trim())){
                    mEmailLayout.setErrorEnabled(false);
                    mEmail.setError(null,successIcon);
                }else {
                    mEmailLayout.setError("Enter valid email address");
                    mEmail.setError(null,errorIcon);
                }
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(AppConfig.validatePassword(mPassword.getText().toString().trim())){
                    mPasswordLayout.setErrorEnabled(false);
                    mPassword.setError(null,successIcon);
                }else {
                    mPasswordLayout.setError("Password must be at least 6 characters");
                    mPassword.setError(null,errorIcon);
                }
            }
        });
        mCPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(AppConfig.validatePassword(mCPassword.getText().toString().trim())){
                    mCPasswordLayout.setErrorEnabled(false);
                    mCPassword.setError(null,successIcon);
                }else {
                    mCPasswordLayout.setError("Password must be at least 6 characters");
                    mCPassword.setError(null,errorIcon);
                }
            }
        });

        //Set onClick Listener here
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateSignUpDetails()){
                    mRelativeLayout.setVisibility(View.INVISIBLE);
                    signUpProgress.show();
                    signUpUser(mName.getText().toString().trim(),
                            mPhone.getText().toString().trim(),
                            mEmail.getText().toString().trim(),
                            mPassword.getText().toString().trim());
                }
            }
        });
    }

    private void getValidationSuccessIcon() {
        successIcon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_succcess);
        if (successIcon != null) {
            successIcon.setBounds(new
                    Rect(0, 0, successIcon.getIntrinsicWidth(), successIcon.getIntrinsicHeight()));
        }
    }

    private void getValidationErrorIcon() {
        errorIcon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error);
        if (errorIcon != null) {
            errorIcon.setBounds(new
                    Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
        }
    }

    private void signUpUser(final String name, final String phone, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    if (mUser != null) {
                        mUserId = mUser.getUid();
                        // Write User Details
                        mDatabaseRef = mDatabase.getReference("Users");
                        UserDetails personalDetails = new UserDetails(name,DEFAULT_DATA,DEFAULT_DATA,DEFAULT_DATA);
                        UserDetails contactDetails = new UserDetails(phone,email);
                        UserDetails address = new UserDetails(DEFAULT_DATA, DEFAULT_DATA,DEFAULT_DATA);
                        UserDetails credentials = new UserDetails(password);
                        mDatabaseRef.child(mUserId).child("Personal Details").setValue(personalDetails);
                        mDatabaseRef.child(mUserId).child("Contact Details").setValue(contactDetails);
                        mDatabaseRef.child(mUserId).child("Contact Details").child("Address").setValue(address);
                        mDatabaseRef.child(mUserId).child("Credentials").setValue(credentials);

                        signUpProgress.dismiss();
                        //Create Intent to go User Page Activity from SignUp Activity here
                        Intent intent = new Intent(SignUpActivity.this,UserPageActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    signUpProgress.dismiss();
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        Snackbar.make
                                (mRelativeLayout,"User already exists",Snackbar.LENGTH_INDEFINITE).
                                setAction("OKAY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                    } catch (FirebaseNetworkException e) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        Snackbar.make
                                (mRelativeLayout,"Check your internet connection !!!",Snackbar.LENGTH_INDEFINITE).
                                setAction("OKAY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    } catch (Exception e) {
                        Snackbar.make(mRelativeLayout,""+e,Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
            }
        });
    }

    private boolean validateSignUpDetails() {
        if (AppConfig.validateName(mName.getText().toString().trim())){
            mNameLayout.setErrorEnabled(false);
            mName.setError(null,successIcon);
            if (AppConfig.validatePhone(mPhone.getText().toString().trim())){
                mPhoneLayout.setErrorEnabled(false);
                mPhone.setError(null,successIcon);
                if(AppConfig.validateEmail(mEmail.getText().toString().trim())){
                    mEmailLayout.setErrorEnabled(false);
                    mEmail.setError(null,successIcon);
                    if (AppConfig.validatePassword(mPassword.getText().toString().trim())){
                        mPasswordLayout.setErrorEnabled(false);
                        mPassword.setError(null,successIcon);
                        if (mPassword.getText().toString().trim().equals(mCPassword.getText().toString().trim())){
                            mCPasswordLayout.setErrorEnabled(false);
                            mCPassword.setError(null,successIcon);
                            return true;
                        }else {
                            mCPasswordLayout.setError("Passwords are not match");
                            mCPassword.setError(null,errorIcon);
                            mCPassword.requestFocus();
                            return false;
                        }
                    }else {
                        mPasswordLayout.setError("Password must be at least 6 characters");
                        mPassword.setError(null,errorIcon);
                        mPassword.requestFocus();
                        return false;
                    }
                }else {
                    mEmailLayout.setError("Enter valid email address");
                    mEmail.setError(null,errorIcon);
                    mEmail.requestFocus();
                    return false;
                }
            }else {
                mPhoneLayout.setError("Enter valid phone number");
                mPhone.setError(null,errorIcon);
                mPhone.requestFocus();
                return false;
            }
        }else {
            mNameLayout.setError("Enter valid name");
            mName.setError(null,errorIcon);
            mName.requestFocus();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signup_page_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signup_terms:
                //TODO: Delete Toast and Write code to implement signup terms menu
                Toast.makeText(this, "Terms & Conditions", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
