package com.rejimalson.finddonors.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.activity.SignInActivity;

public class SettingsFragment extends Fragment {

    View settingsView;

    Button btn_Logout;

    private FirebaseAuth mAuth;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);

        btn_Logout = (Button)settingsView.findViewById(R.id.logout);
        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return settingsView;
    }

}
