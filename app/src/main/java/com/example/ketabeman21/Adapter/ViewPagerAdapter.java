package com.example.ketabeman21.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.ketabeman21.Fragments.BookFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 3;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return BookFragment.newInstance();
            case 1:
              //  return BookFragment.newInstance();
            case 2:
               // return CodesFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return BookFragment.TITLE;

            case 1:
                return BookFragment.TITLE;

            case 2:
               // return CodesFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}
