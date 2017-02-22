package com.rejimalson.finddonors.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.fragment.MessagesFragment;
import com.rejimalson.finddonors.fragment.ProfileFragment;
import com.rejimalson.finddonors.fragment.SearchFragment;
import com.rejimalson.finddonors.fragment.SettingsFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class UserPageActivity extends AppCompatActivity {
    //Declare the instance here
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        //Find View Id here
        mBottomBar = (BottomBar)findViewById(R.id.bottomBar);

        //Set onTabSelectListener Here
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch(tabId) {
                    case R.id.btm_tab_profile:
                        showProfileFragment();
                        break;
                    case R.id.btm_tab_search:
                        showSearchFragment();
                        break;
                    case R.id.btm_tab_messages:
                        showMessagesFragment();
                        break;
                    case R.id.btm_tab_settings:
                        showSettingsFragment();
                        break;
                }
            }
        });
    }

    private void showSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.up_fragment_container,settingsFragment);
        fragmentTransaction.commit();
    }

    private void showMessagesFragment() {
        MessagesFragment messagesFragment = new MessagesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.up_fragment_container,messagesFragment);
        fragmentTransaction.commit();
    }

    private void showSearchFragment() {
        SearchFragment searchFragment = new SearchFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.up_fragment_container,searchFragment);
        fragmentTransaction.commit();
    }

    private void showProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.up_fragment_container,profileFragment);
        fragmentTransaction.commit();
    }
}
