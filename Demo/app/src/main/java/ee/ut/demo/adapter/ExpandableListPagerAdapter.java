package ee.ut.demo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ee.ut.demo.fragment.EventFragment;

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

    private int mDate;

    public ExpandableListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return EventFragment.newInstance(mDate);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        mDate = position + START_INDEX;
        return mDate + " " + MONTH_TEXT;
    }
}
