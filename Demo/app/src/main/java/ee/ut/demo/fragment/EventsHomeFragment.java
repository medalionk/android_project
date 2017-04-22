package ee.ut.demo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.adapter.ExpandableListPagerAdapter;

/**
 * Created by Bilal Abdullah on 4/18/2017.
 */

public class EventsHomeFragment extends Fragment{
    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Bind(R.id.navigation)
    BottomNavigationView navigation;

    private ExpandableListPagerAdapter mAdapter;
    private Fragment mFragment;

    public static EventsHomeFragment newInstance() {
        return new EventsHomeFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_home, container, false);

        ButterKnife.bind(this, rootView);

        mAdapter = new ExpandableListPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        return rootView;
    }

    private EventsHomeFragment getEventsHomeFragment(){
        return this;
    }

    private void removeFragment(){
        if (mFragment != null){
            getChildFragmentManager().beginTransaction().remove(mFragment).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    removeFragment();
                    mAdapter = new ExpandableListPagerAdapter(getChildFragmentManager());
                    mViewPager.setAdapter(mAdapter);

                    return true;
                case R.id.navigation_saved_events:

                    mFragment = FavoriteFragment.newInstance();
                    fragmentTransaction.replace(R.id.frame, mFragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;
                case R.id.navigation_map:

                    mFragment = new MapFragment();
                    fragmentTransaction.replace(R.id.frame, mFragment);
                    fragmentTransaction.commitAllowingStateLoss();

                    return true;
            }
            return false;
        }

    };
}
