package com.rejimalson.finddonors.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejimalson.finddonors.R;

public class SettingsFragment extends Fragment {

    View settingsView;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        return settingsView;
    }

}
