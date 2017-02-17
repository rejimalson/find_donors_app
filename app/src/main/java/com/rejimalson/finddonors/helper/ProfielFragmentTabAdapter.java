package com.rejimalson.finddonors.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rejimalson.finddonors.fragment.AboutUserFragment;
import com.rejimalson.finddonors.fragment.MyActivitiesFragment;

public class ProfielFragmentTabAdapter extends FragmentStatePagerAdapter {
    //Declare Instance
    private int mTabCount;

    public ProfielFragmentTabAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mTabCount = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AboutUserFragment();
            case 1:
                return new MyActivitiesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }
}
