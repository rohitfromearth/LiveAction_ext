package com.example.liveaction_ext;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 4;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        // Return the fragment instance based on the tab position
        switch (position) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
            case 2:
                return new Fragment3();
            case 3:
                return new Fragment4();

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
            case 3:
                return "Year";

            default:
                return null;
        }
    }
}
