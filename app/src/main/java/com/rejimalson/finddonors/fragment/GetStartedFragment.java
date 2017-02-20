package com.rejimalson.finddonors.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejimalson.finddonors.R;

public class GetStartedFragment extends Fragment {

    private View myView;
    public GetStartedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_get_started, container,false);

        return myView;
    }

}
