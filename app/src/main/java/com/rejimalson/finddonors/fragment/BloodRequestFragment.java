package com.rejimalson.finddonors.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejimalson.finddonors.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BloodRequestFragment extends Fragment {

    View bloodRequestView;
    public BloodRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bloodRequestView = inflater.inflate(R.layout.fragment_blood_request, container, false);

        return bloodRequestView;
    }

}
