package com.rejimalson.finddonors.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.fragment.NameGenderFragment;

public class OnBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        showNameGenderFragment();
    }

    private void showNameGenderFragment() {
        NameGenderFragment nameGenderFragment = new NameGenderFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.on_board_pages_container,nameGenderFragment);
        fragmentTransaction.commit();
    }
}
