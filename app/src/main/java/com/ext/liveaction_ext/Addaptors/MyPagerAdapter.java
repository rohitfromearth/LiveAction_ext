package com.ext.liveaction_ext.Addaptors;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ext.liveaction_ext.frags.DayFragment;
import com.ext.liveaction_ext.frags.MonthFragment;
import com.ext.liveaction_ext.frags.WeekFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 3;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        // Return the fragment instance based on the tab position
        switch (position) {
            case 0:
                return new DayFragment();
            case 1:
                return new WeekFragment();
            case 2:
                return new MonthFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the tab title based on the tab position
        switch (position) {
            case 0:
                return "Day";
            case 1:
                return "Week";
            case 2:
                return "Month";

            default:
                return null;
        }
    }
}
