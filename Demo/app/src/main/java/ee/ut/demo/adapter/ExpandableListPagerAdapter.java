package ee.ut.demo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ee.ut.demo.fragment.ExpandableListFragment;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */

public class ExpandableListPagerAdapter extends FragmentStatePagerAdapter {

    private final String MONTH_TEXT = "aprill";
    private final int START_INDEX = 24;
    private final int PAGE_COUNT = 7;

    public ExpandableListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ExpandableListFragment();
        Bundle args = new Bundle();
        args.putInt(ExpandableListFragment.ARG_OBJECT, i + START_INDEX);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "" + (position + START_INDEX) + " " + MONTH_TEXT;
    }
}
