package com.rejimalson.finddonors.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.rejimalson.finddonors.activity.UserPageActivity;
import com.rejimalson.finddonors.helper.ProfileFragmentTabAdapter;
import com.rejimalson.finddonors.helper.UserDetails;
import com.rejimalson.finddonors.helper.UserInfo;

public class ProfileFragment extends Fragment {

    View profileView;

    TabLayout mTabLayout;
    ViewPager mViewPager;

    TextView tv_FullName, tv_Phone;
    CoordinatorLayout mMainLayout;

    String mFullName;
    String mPhoneNumber;

    //Declare Firebase instance here
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    String mUserId;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

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
                    tv_FullName.setText(mFullName);
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
                    tv_Phone.setText(mPhoneNumber);
                    mMainLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mMainLayout = (CoordinatorLayout)profileView.findViewById(R.id.profile_fragment_main_layout);
        tv_FullName = (TextView)profileView.findViewById(R.id.userName);
        tv_Phone = (TextView)profileView.findViewById(R.id.userPhone);
        //Toast.makeText(getActivity(), "after view - "+mFullName, Toast.LENGTH_SHORT).show();
        //tv_FullName.setText(mFullName);

        mTabLayout = (TabLayout)profileView.findViewById(R.id.profileTabLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("About Me"));
        mTabLayout.addTab(mTabLayout.newTab().setText("My Activities"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager)profileView.findViewById(R.id.profileTabPageContainer);
        ProfileFragmentTabAdapter profileFragmentTabAdapter =
                new ProfileFragmentTabAdapter(getFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(profileFragmentTabAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        return profileView;
    }
}
