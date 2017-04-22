package ee.ut.demo.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.fragment.EventsHomeFragment;
import ee.ut.demo.fragment.FeedbackFragment;
import ee.ut.demo.fragment.HomeFragment;
import ee.ut.demo.fragment.PlaylistFragment;
import ee.ut.demo.injector.component.AlarmComponent;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerAlarmComponent;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.AlarmModule;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.presenter.AlarmPresenter;
import ee.ut.demo.mvp.view.HomeView;

import static ee.ut.demo.R.id.toolbar;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class HomeActivity extends AppCompatActivity implements HomeView {

    private static final String ARG_FRAGMENT = "fragment";
    private static final String ARG_CURRENT_PAGE = "currentPage";
    private static final String ARG_NAV_IDX = "navItemIndex";

    private static final String TAG_HOME = "home";
    private static final String TAG_FEEDBACK = "feedback";
    private static final String TAG_PLAYLIST = "playlist";
    private static final String TAG_EVENTS = "events";

    private final int PERMISSIONS_REQUEST = 0;

    public static String CURRENT_TAG = TAG_HOME;
    public static int mNavItemIndex = 0;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private View mNavHeader;
    private ImageView mImgNavHeaderBg;
    private Toolbar mToolbar;
    private Fragment mFragment;

    private String[] mActivityTitles;
    ArrayList<Integer> Day = new ArrayList<Integer>();
    ArrayList<Integer> Time = new ArrayList<Integer>();


    private boolean mShouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Inject
    AlarmPresenter mEventPresenter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mToolbar = (Toolbar) findViewById(toolbar);
        setSupportActionBar(mToolbar);

        mHandler = new Handler();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavHeader = mNavigationView.getHeaderView(0);
        mImgNavHeaderBg = (ImageView) mNavHeader.findViewById(R.id.img_header_bg);

        mActivityTitles = getResources().getStringArray(R.array.home_menu);

        injectDependencies();

        mEventPresenter.onCreate();

        ButterKnife.bind(this);

        initPresenter();
        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            mNavItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            mFragment = getHomeFragment();
        }else {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, ARG_FRAGMENT);
            CURRENT_TAG = savedInstanceState.getString(ARG_CURRENT_PAGE);
            mNavItemIndex = savedInstanceState.getInt(ARG_NAV_IDX);
        }

        loadHomeFragment();
        requestPermission();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, ARG_FRAGMENT, mFragment);
        outState.putString(ARG_CURRENT_PAGE, CURRENT_TAG);
        outState.putInt(ARG_NAV_IDX, mNavItemIndex);
    }

    private void loadNavHeader() {
        mImgNavHeaderBg.setImageDrawable(getDrawable(R.drawable.header1));
    }

    /**
     * Request user's permission to get location
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[] {Manifest.permission.INTERNET}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }else {
                    Toast.makeText(getApplicationContext(), "Internet Permission Required", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();
        mDrawer.closeDrawers();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            return;
        }

        showFragment(mFragment);
    }

    private void showFragment(final Fragment fragment){
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        mHandler.post(mPendingRunnable);
        mDrawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private  void injectDependencies() {
        ApplicationComponent appComponent = ((TartuApplication) this.getApplication())
                .getApplicationComponent();
        AlarmComponent favouriteComponent = DaggerAlarmComponent.builder()
                .alarmModule(new AlarmModule())
                .activityModule(new ActivityModule(this))
                .applicationComponent(appComponent)
                .build();

        favouriteComponent.inject(this);
    }

    private Fragment getHomeFragment() {
        switch (mNavItemIndex) {
            case 0:
                return HomeFragment.newInstance();
            case 1:
                return EventsHomeFragment.newInstance();
            case 2:
                return PlaylistFragment.newInstance();
            case 3:
                return FeedbackFragment.newInstance();
            default:
                return HomeFragment.newInstance();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(mActivityTitles[mNavItemIndex]);
    }

    private void selectNavMenu() {
        mNavigationView.getMenu().getItem(mNavItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        mNavItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;

                    case R.id.event_Schedules:
                        mNavItemIndex = 1;
                        CURRENT_TAG = TAG_EVENTS;
                        break;

                    case R.id.nav_playlist:
                        mNavItemIndex = 2;
                        CURRENT_TAG = TAG_PLAYLIST;
                        break;

                    case R.id.nav_feedback:
                        mNavItemIndex = 3;
                        CURRENT_TAG = TAG_FEEDBACK;
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.sponsors:
                        startActivity(new Intent(HomeActivity.this, SponsorsActivity.class));
                        mDrawer.closeDrawers();
                        return true;
                    default:
                        mNavItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                //menuItem.setChecked(true);

                mFragment = getHomeFragment();
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }

        if (mShouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (mNavItemIndex != 0) {
                mNavItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                mFragment = getHomeFragment();
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    private void initPresenter() {
        mEventPresenter.attachView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventPresenter.onStop();
    }


    @Override
    public void onStart() {
        super.onStart();
        mEventPresenter.onStart();
    }


    @Override
    public void addFavouriteEvents(final List<Event> events) {
                Time.clear();
                Day.clear();
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < events.size() ; i++){
                    events.get(i).getStartTime();


                    String[] parts = events.get(i).getStartTime().split(":");
                    String[] day = events.get(i).getDetails().getDate().split(" ");

                    int part1 = Integer.parseInt(parts[0]); // 004-
                    int second = Integer.parseInt(day[2]);

                    Day.add(second);
                    Time.add(part1);

                }

                loadAlarm();

            }
        };

        this.runOnUiThread(myRunnable);
    }

    public void loadAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar notification_time = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        Log.d ("my array",Arrays.toString(Day.toArray()));
     if (Time.size()  != 0) {
         for (int i = 0; i < Time.size(); i++) {
             int event_time;
             if (Time.get(i) != 00) {
                 event_time = Time.get(i) - 1;
             } else event_time = Time.get(i);

             notification_time.set(Calendar.MONTH, 3);
             notification_time.set(Calendar.DAY_OF_MONTH, Day.get(i));
             notification_time.set(Calendar.HOUR_OF_DAY, event_time);
             notification_time.set(Calendar.MINUTE, 00);
             notification_time.set(Calendar.SECOND, 0);

             if (now.after(notification_time)) {

                 Time.remove(i);
                 Day.remove(i);

             } else
                 alarmManager.setExact(AlarmManager.RTC_WAKEUP, notification_time.getTimeInMillis(), broadcast);


         }
         //TODO (a call to DB to retrieve the event date and compare the current date to the date retrieved. If same, then notify the app user)}
     }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showEmpty() {

    }
}