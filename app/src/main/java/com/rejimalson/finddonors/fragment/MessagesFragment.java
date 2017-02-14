package com.rejimalson.finddonors.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rejimalson.finddonors.R;

public class MessagesFragment extends Fragment {

    View messagesView;
    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        messagesView = inflater.inflate(R.layout.fragment_messages, container, false);

        return messagesView;
    }

}
