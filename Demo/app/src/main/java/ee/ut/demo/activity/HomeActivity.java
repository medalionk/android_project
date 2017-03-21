package ee.ut.demo.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;

import ee.ut.demo.R;
import ee.ut.demo.fragment.HomeFragment;
import ee.ut.demo.fragment.MapFragment;
import ee.ut.demo.fragment.NotificationFragment;
import ee.ut.demo.fragment.SettingFragment;

import static ee.ut.demo.R.id.toolbar;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,MapFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,
SettingFragment.OnFragmentInteractionListener{

    private static final String TAG_HOME = "home";
    private static final String TAG_MAP = "map";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";

    public static String CURRENT_TAG = TAG_HOME;
    public static int mNavItemIndex = 0;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private View mNavHeader;
    private ImageView mImgNavHeaderBg;
    private Toolbar mToolbar;

    private String[] mActivityTitles;

    private boolean mShouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

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
        loadNavHeader();
        loadAlarm();
        setUpNavigationView();

        if (savedInstanceState == null) {
            mNavItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        mImgNavHeaderBg.setImageDrawable(getDrawable(R.drawable.header1));
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            mDrawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };


        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        mDrawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (mNavItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 3:
                //notifications
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 5:
                //map
                MapFragment mapFragment = new MapFragment();
                return mapFragment;
            case 6:
                // settings
                SettingFragment settingFragment = new SettingFragment();
                return settingFragment;

            default:
                return new HomeFragment();
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

                    case R.id.nav_notifications:
                        mNavItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;

                    case R.id.map:
                        mNavItemIndex =  5;
                        CURRENT_TAG = TAG_MAP;
                        break;

                    case R.id.nav_settings:
                        mNavItemIndex = 6;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;

                    case R.id.nav_playlist:
                        Toast.makeText(getApplicationContext(), "Event Playlist", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this,EventsActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.event_Schedules:
                        startActivity(new Intent(HomeActivity.this,EventsActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.personal_Schedules:
                        Toast.makeText(getApplicationContext(), "Personal Schedules Clicked(not created)", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this,ScheduleActivity.class));
                        mDrawer.closeDrawers();
                        return true;

                    case R.id.nav_about_us:
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
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
                menuItem.setChecked(true);

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
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (mNavItemIndex == 0) {
            //menu to display at home fragment, currently leaving this empty
        }

        if (mNavItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadAlarm() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar notification_time = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        notification_time.set(Calendar.HOUR_OF_DAY,8);
        notification_time.set(Calendar.MINUTE, 00);
        notification_time.set(Calendar.SECOND, 0);

        if (now.after(notification_time)) {
            Log.d("notification","Day incremented by one");
            notification_time.add(Calendar.DATE, 1);
        }
qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq
        //TODO (a call to DB to retrieve the event date and compare the current date to the date retrieved. If same, then notify the app user)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,notification_time.getTimeInMillis(), broadcast);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}