package com.rejimalson.finddonors.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;
import com.rejimalson.finddonors.helper.EditextErrorIcon;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    //Declare the instance here
    private TextInputLayout mEmailLayout,mPasswordLayout;
    private EditextErrorIcon mEmail, mPassword;
    private TextView mForgotPwd, mSignUp;
    private Button mSignIn;
    private RelativeLayout mRelativeLayout;

    private Drawable errorIcon;
    private ProgressDialog signInProgress;

    //Firebase Instance
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Firebase instance
        mAuth = FirebaseAuth.getInstance();
        //Initialize Firebase Auth Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null){
                    String email = mUser.getEmail();
                    Toast.makeText(SignInActivity.this, ""+email, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInActivity.this,UserPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        setContentView(R.layout.activity_sign_in);
        //Get the error and success icons
        getValidationErrorIcon();

        //Initialize the Progress Dialog
        signInProgress = new ProgressDialog(this);
        signInProgress.setMessage("Signing In Please wait...");
        signInProgress.setCancelable(false);

        //Find View Id here
        mEmailLayout = (TextInputLayout)findViewById(R.id.si_email_input_layout);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.si_pwd_input_layout);
        mEmail = (EditextErrorIcon)findViewById(R.id.si_input_email);
        mPassword = (EditextErrorIcon)findViewById(R.id.si_input_pwd);
        mForgotPwd = (TextView)findViewById(R.id.sign_in_frgt_pwd_id);
        mSignUp = (TextView)findViewById(R.id.tv_signup_id);
        mSignIn = (Button)findViewById(R.id.sign_in_btn_id);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.mainRelativeLayout);

        //Add text watcher here
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
                    mEmail.setError(null,null);
                }else {
                    mEmailLayout.setError("Enter valid email address");
                    mEmail.setError(null,errorIcon);
                }
            }
        });

        //Set onClick listener Here
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSignInDetailsEmpty()){
                    mRelativeLayout.setVisibility(View.INVISIBLE);
                    signInProgress.show();
                    signInUser(mEmail.getText().toString().trim(),mPassword.getText().toString().trim());
                }
            }
        });
        mForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Write code for Forgot Password process on Forgot Password TextView click
                Toast.makeText(SignInActivity.this, "Forgot Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInProgress.dismiss();
                if (task.isSuccessful()){
                    mAuth.addAuthStateListener(mAuthListener);
                }else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        Snackbar.make(mRelativeLayout,"Incorrect Email or Password",Snackbar.LENGTH_LONG).show();
                    } catch (FirebaseAuthInvalidUserException e) {
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        Snackbar.make(mRelativeLayout,"User does not exists",Snackbar.LENGTH_LONG).show();
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
                        Snackbar.make(mRelativeLayout,""+e,Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean isSignInDetailsEmpty() {
        if(AppConfig.validateEmail(mEmail.getText().toString().trim())){
            mEmailLayout.setErrorEnabled(false);
            mEmail.setError(null,null);
            if(mPassword.getText().toString().trim().length() != 0){
                mPasswordLayout.setErrorEnabled(false);
                mPassword.setError(null,null);
                return false;
            }else {
                mPasswordLayout.setError("Enter the password");
                mPassword.setError(null,errorIcon);
                mPassword.requestFocus();
                return true;
            }
        }else {
            mEmailLayout.setError("Enter valid email address");
            mEmail.setError(null,errorIcon);
            mEmail.requestFocus();
            return true;
        }
    }

    private void getValidationErrorIcon() {
        errorIcon = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_error);
        if (errorIcon != null) {
            errorIcon.setBounds(new
                    Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
