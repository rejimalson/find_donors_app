package com.rejimalson.finddonors.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.config.AppConfig;

import static com.rejimalson.finddonors.R.id.gender;

public class NameGenderFragment extends Fragment {

    View nameGenderView;
    Spinner genderSpinner,bloodGroupSpinner;
    public NameGenderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        nameGenderView = inflater.inflate(R.layout.fragment_name_gender, container, false);

        genderSpinner = (Spinner)nameGenderView.findViewById(gender);
        bloodGroupSpinner = (Spinner)nameGenderView.findViewById(R.id.blood_grp);

        ArrayAdapter gender_Adapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_dropdown_item_1line, AppConfig.GENDER_LIST);
        gender_Adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setAdapter(gender_Adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = AppConfig.GENDER_LIST[position];
                Toast.makeText(getActivity(), ""+gender, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ArrayAdapter blood_group_adapter = new ArrayAdapter<>
                (getActivity(),android.R.layout.simple_dropdown_item_1line,AppConfig.BLOOD_GROUP_LIST);
        blood_group_adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        bloodGroupSpinner.setAdapter(blood_group_adapter);
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String blood_group = AppConfig.BLOOD_GROUP_LIST[position];
                Toast.makeText(getActivity(), ""+blood_group, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        return nameGenderView;
    }

}
