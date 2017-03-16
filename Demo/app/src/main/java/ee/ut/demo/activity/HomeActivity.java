package ee.ut.demo.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ee.ut.demo.R;
import ee.ut.demo.fragment.HomeFragment;
import ee.ut.demo.fragment.MapFragment;
import ee.ut.demo.fragment.NotificationFragment;
import ee.ut.demo.fragment.SettingFragment;

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

    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    private Toolbar toolbar;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        activityTitles = getResources().getStringArray(R.array.home_menu);
        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        imgNavHeaderBg.setImageDrawable(getDrawable(R.drawable.tartu));
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
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

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
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
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.map:
                        navItemIndex =  5;
                        CURRENT_TAG = TAG_MAP;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_playlist:
                        Toast.makeText(getApplicationContext(), "Event Playlist", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this,EventsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.event_Schedules:
                        Toast.makeText(getApplicationContext(), "Event Schedules Clicked(not created)", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this,EventsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.personal_Schedules:
                        Toast.makeText(getApplicationContext(), "Personal Schedules Clicked(not created)", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(HomeActivity.this,SchedulesActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about_us:
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.sponsors:
                        startActivity(new Intent(HomeActivity.this, SponsorsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
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
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (navItemIndex == 0) {
            //menu to display at home fragment, currently leaving this empty
        }

        if (navItemIndex == 3) {
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

    @Override
    public void onFragmentInteraction(Uri uri){

    }
}