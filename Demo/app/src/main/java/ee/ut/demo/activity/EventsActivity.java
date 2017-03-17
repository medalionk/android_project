package ee.ut.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.adapter.ExpandableListPagerAdapter;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerEventsComponent;
import ee.ut.demo.injector.component.EventsComponent;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.EventsModule;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.presenter.EventsPresenter;
import ee.ut.demo.mvp.view.EventsView;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class EventsActivity extends AppCompatActivity implements EventsView{

    @Inject
    EventsPresenter eventsPresenter;

    @Bind(R.id.pager)
    ViewPager mViewPager;

    //@Bind(R.id.ar_loading)
    //View loadingView;

    ExpandableListPagerAdapter mExpandableListPagerAdapter;
    private EventsComponent eventsComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_events);

        ButterKnife.bind(this);

        ApplicationComponent appComponent = ((TartuApplication)
                getApplication()).getApplicationComponent();
        eventsComponent = DaggerEventsComponent.builder()
                .eventsModule(new EventsModule())
                .activityModule(new ActivityModule(this))
                .applicationComponent(appComponent)
                .build();
        eventsComponent.inject(this);
        initializePresenter();

        mExpandableListPagerAdapter = new ExpandableListPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mExpandableListPagerAdapter);
    }

    private void initializePresenter() {
        eventsPresenter.attachView(this);
    }

    public EventsComponent getEventsComponent() {
        return eventsComponent;
    }

    public void addEvents(Collection<Event> events) {
//        mExpandableListPagerAdapter.addAll(events);
//        hideLoading();
//        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showLoading() {
//        fab.setVisibility(View.GONE);
//        viewPager.setVisibility(View.INVISIBLE);
//        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EventsActivity.this, R.string.error_loading, Toast.LENGTH_LONG).show();
            }
        };
        runOnUiThread(myRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //eventsPresenter.onStop();
    }

    @Override
    public void showEvents(final List<Event> events) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                //addEvents(events);
            }
        };
        runOnUiThread(myRunnable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = new Intent(this, HomeActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.from(this).addNextIntent(upIntent).startActivities();
                    finish();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
