package com.rejimalson.finddonors.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;
import com.rejimalson.finddonors.helper.EditextErrorIcon;

public class SignUpActivity extends AppCompatActivity {
    //Declare instance here
    private TextInputLayout mEmailLayout,mPasswordLayout,mCPasswordLayout;
    private EditextErrorIcon mEmail, mPassword, mCPassword;
    private Button mSignUp;
    private RelativeLayout mRelativeLayout;

    private Drawable successIcon, errorIcon;
    private ProgressDialog signUpProgress;

    //Firebase Instance
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Set Title of the activity
        this.setTitle(R.string.signUpActivityTitle);

        //Enable back button on action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //Get the error and success icons
        getValidationErrorIcon();
        getValidationSuccessIcon();

        //Initialize the Progress Dialog
        signUpProgress = new ProgressDialog(this);
        signUpProgress.setMessage("Signing Up Please wait...");
        signUpProgress.setCancelable(false);

        //Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();

        // Find View Id here
        mEmailLayout = (TextInputLayout)findViewById(R.id.su_email_input_layout);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.su_pwd_input_layout);
        mCPasswordLayout = (TextInputLayout)findViewById(R.id.su_cpwd_input_layout);
        mEmail = (EditextErrorIcon) findViewById(R.id.su_input_email);
        mPassword = (EditextErrorIcon) findViewById(R.id.su_input_pwd);
        mCPassword = (EditextErrorIcon) findViewById(R.id.su_input_cpwd);
        mSignUp = (Button)findViewById(R.id.sign_up_btn_id);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);

        //Set Text watcher here
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
                    mPasswordLayout.setError("Password must be at least 5 characters");
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
                    mCPasswordLayout.setError("Password must be at least 5 characters");
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
                    signUpUser(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
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

    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgress.dismiss();
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this,UserPageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        //TODO: Write logic for user collision exception
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignUpActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean validateSignUpDetails() {
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
                mPasswordLayout.setError("Password must be at least 5 characters");
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
        getMenuInflater().inflate(R.menu.menu_signup_terms,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_signup_terms:
                //TODO: Write code to implement signup terms menu
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
