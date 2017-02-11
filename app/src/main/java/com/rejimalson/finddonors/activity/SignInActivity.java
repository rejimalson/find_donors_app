package com.rejimalson.finddonors.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.helper.EditextErrorIcon;

import org.w3c.dom.Text;

public class SignInActivity extends AppCompatActivity {
    //Declare the instance here
    private TextInputLayout mEmailLayout,mPasswordLayout;
    private EditextErrorIcon mEmail, mPassword;
    private TextView mForgotPwd, mSignUp;
    private Button mSignIn;

    //Firebase Instance
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Initialize Firebase instance
        mAuth = FirebaseAuth.getInstance();
        //Initialize Firebase Auth Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null){
                    Intent intent = new Intent(SignInActivity.this,UserPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        //Find View Id here
        mEmailLayout = (TextInputLayout)findViewById(R.id.si_email_input_layout);
        mPasswordLayout = (TextInputLayout)findViewById(R.id.si_pwd_input_layout);
        mEmail = (EditextErrorIcon)findViewById(R.id.si_input_email);
        mPassword = (EditextErrorIcon)findViewById(R.id.si_input_pwd);
        mForgotPwd = (TextView)findViewById(R.id.sign_in_frgt_pwd_id);
        mSignUp = (TextView)findViewById(R.id.tv_signup_id);
        mSignIn = (Button)findViewById(R.id.sign_in_btn_id);

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
                //TODO: Write code for SignIn process on SignIn Button click
            }
        });
        mForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Write code for Forgot Password process on Forgot Password TextView click
            }
        });
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
}
