package com.rejimalson.finddonors.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    ImageView iv_PopUpMenu_Personal, iv_PopUpMenu_Contact;
    ImageView iv_PersonalAccessIcon, iv_ContactAccessIcon;

    CardView cv_PersonalDetails;

    //Declare Firebase instance here
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    String mUserId;

    String mFullName, mBloodGroup, mBirthday, mGender;
    String mPhoneNumber, mEmailAddress;
    Boolean mPersonalDetailsPrivate, mContactDetailsPrivate;

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
            mDatabaseRef = mDatabase.getReference().child("Users").child(mUserId).child("Account Settings");
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    mPersonalDetailsPrivate = userInfo.getPersonalDetailsPrivate();
                    mContactDetailsPrivate = userInfo.getContactDetailsPrivate();
                    if (mPersonalDetailsPrivate){
                        iv_PersonalAccessIcon.setBackgroundResource(R.drawable.ic_private);
                    }else {
                        iv_PersonalAccessIcon.setBackgroundResource(R.drawable.ic_public);
                    }
                    if (mContactDetailsPrivate){
                        iv_ContactAccessIcon.setBackgroundResource(R.drawable.ic_private);
                    }else {
                        iv_ContactAccessIcon.setBackgroundResource(R.drawable.ic_public);
                    }
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

        cv_PersonalDetails = (CardView)aboutView.findViewById(R.id.personal_details_cardView);

        iv_PopUpMenu_Personal = (ImageView)aboutView.findViewById(R.id.personal_details_edit_icon);
        iv_PopUpMenu_Contact = (ImageView)aboutView.findViewById(R.id.contact_details_edit_icon);

        iv_PersonalAccessIcon = (ImageView)aboutView.findViewById(R.id.personal_details_access_icon);
        iv_ContactAccessIcon = (ImageView)aboutView.findViewById(R.id.contact_details_access_icon);

        iv_PopUpMenu_Personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(),iv_PopUpMenu_Personal,Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.about_me_popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mDatabaseRef = mDatabase.getReference().child("Users");
                        switch (item.getItemId()){
                            case R.id.edit_details:
                                Toast.makeText(getActivity(), "Personal", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.make_private:
                                mDatabaseRef.child(mUserId).child("Account Settings").child("personalDetailsPrivate").setValue(true);
                                return true;
                            case R.id.make_public:
                                mDatabaseRef.child(mUserId).child("Account Settings").child("personalDetailsPrivate").setValue(false);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        iv_PopUpMenu_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(),iv_PopUpMenu_Contact,Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.about_me_popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        mDatabaseRef = mDatabase.getReference().child("Users");
                        switch (item.getItemId()){
                            case R.id.edit_details:
                                Toast.makeText(getActivity(), "Contact", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.make_private:
                                mDatabaseRef.child(mUserId).child("Account Settings").child("contactDetailsPrivate").setValue(true);
                                return true;
                            case R.id.make_public:
                                mDatabaseRef.child(mUserId).child("Account Settings").child("contactDetailsPrivate").setValue(false);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        return aboutView;
    }

}
