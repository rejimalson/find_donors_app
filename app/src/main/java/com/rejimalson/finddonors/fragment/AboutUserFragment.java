package com.rejimalson.finddonors.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.helper.UserInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUserFragment extends Fragment {

    View aboutView;

    TextView tv_FullName, tv_BloodGroup,tv_Birthday, tv_Gender;
    TextView tv_Phone_Number, tv_Email_Address;

    //Declare Firebase instance here
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    String mUserId;

    String mFullName, mBloodGroup, mBirthday, mGender;
    String mPhoneNumber, mEmailAddress;

    public AboutUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        aboutView = inflater.inflate(R.layout.fragment_about_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            mUserId = mUser.getUid();
            mDatabaseRef = mDatabase.getReference().child("Users").child(mUserId).child("Personal Details");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    mFullName = userInfo.getFullName();
                    mBloodGroup = userInfo.getBloodGroup();
                    mBirthday = userInfo.getBirthDay();
                    mGender = userInfo.getGender();
                    tv_FullName.setText(mFullName);
                    tv_BloodGroup.setText(mBloodGroup);
                    tv_Birthday.setText(mBirthday);
                    tv_Gender.setText(mGender);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            mDatabaseRef = mDatabase.getReference().child("Users").child(mUserId).child("Contact Details");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    mPhoneNumber = "+91 - "+userInfo.getPhone();
                    mEmailAddress = userInfo.getEmail();
                    tv_Phone_Number.setText(mPhoneNumber);
                    tv_Email_Address.setText(mEmailAddress);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        tv_FullName = (TextView)aboutView.findViewById(R.id.name_text);
        tv_BloodGroup = (TextView)aboutView.findViewById(R.id.blood_grp_text);
        tv_Birthday = (TextView)aboutView.findViewById(R.id.bday_text);
        tv_Gender = (TextView)aboutView.findViewById(R.id.gender_text);

        tv_Phone_Number = (TextView)aboutView.findViewById(R.id.ph_text);
        tv_Email_Address = (TextView)aboutView.findViewById(R.id.mail_text);

        return aboutView;
    }

}
