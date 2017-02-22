package com.rejimalson.finddonors.fragment;

import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.helper.ProfileFragmentTabAdapter;

public class ProfileFragment extends Fragment {

    TabLayout mTabLayout;
    ViewPager mViewPager;

    View profileView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

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
