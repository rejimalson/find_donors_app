package com.rejimalson.finddonors.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.helper.ProfielFragmentTabAdapter;

public class ProfileFragment extends Fragment {

    View profileView;
    TabLayout mTablayout;
    ViewPager mViewPager;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        mTablayout = (TabLayout)profileView.findViewById(R.id.profileTabLayout);
        mTablayout.addTab(mTablayout.newTab().setText("About Me"));
        mTablayout.addTab(mTablayout.newTab().setText("My Activities"));
        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager)profileView.findViewById(R.id.profileTabPageContainer);
        ProfielFragmentTabAdapter profielFragmentTabAdapter =
                new ProfielFragmentTabAdapter(getFragmentManager(),mTablayout.getTabCount());
        mViewPager.setAdapter(profielFragmentTabAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
