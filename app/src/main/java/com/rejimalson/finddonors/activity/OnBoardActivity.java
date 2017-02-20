package com.rejimalson.finddonors.activity;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rejimalson.finddonors.R;
import com.rejimalson.finddonors.fragment.GetStartedFragment;

public class OnBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        showGetStartedFragment();
    }

    private void showGetStartedFragment() {
        GetStartedFragment getStartedFragment = new GetStartedFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.getStartedPageContainer,getStartedFragment);
        fragmentTransaction.commit();
    }
}
