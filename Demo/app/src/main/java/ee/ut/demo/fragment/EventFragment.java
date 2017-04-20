package ee.ut.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.adapter.CustomExpandableListAdapter;
import ee.ut.demo.adapter.FavoriteListener;
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
public class EventFragment extends Fragment implements EventsView, FavoriteListener {

    @Inject
    EventsPresenter mEventsPresenter;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @Bind(R.id.loading)
    View mLoadingView;

    @Bind(R.id.expandable_list)
    ExpandableListView mExpandableListView;

    @Bind(R.id.empty_list)
    View mEmptyListView;

    @Bind(R.id.error)
    View mErrorListView;

    private static final String PAGE = "page_number";

    private CustomExpandableListAdapter mExpandableListAdapter;
    private int mPage = -1;

    public static EventFragment newInstance(int page) {
        EventFragment roomFragment = new EventFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, page);
        roomFragment.setArguments(bundle);

        return roomFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE, mPage);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        ButterKnife.bind(this, rootView);

        initAdapter();
        initListView();

        if (savedInstanceState != null) {
            mPage = savedInstanceState.getInt(PAGE);
        } else {
            mPage = getArguments().getInt(PAGE);
        }

        initPresenter();
        setupRefreshLayout();

        return rootView;
    }

    private  void injectDependencies() {
        ApplicationComponent appComponent = ((TartuApplication) getActivity().getApplication())
                .getApplicationComponent();
        EventsComponent eventsComponent = DaggerEventsComponent.builder()
                .eventsModule(new EventsModule())
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(appComponent)
                .build();

        eventsComponent.inject(this);
    }

    private void initAdapter() {
        if (mExpandableListAdapter == null) {
            mExpandableListAdapter = new CustomExpandableListAdapter(getActivity());
        }
    }

    private void initListView() {
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListAdapter.setFavoriteListener(this);

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView
                        .getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                return false;
            }
        });
    }

    private void setupRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEventsPresenter.onRefresh();
            }
        });
    }

    private void initPresenter() {
        mEventsPresenter.attachView(this);
        mEventsPresenter.setPage(mPage);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventsPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        ////
        //mEventsPresenter.setPage(mPage);
        mEventsPresenter.onStart();
    }

    public void addEvents(Collection<Event> events) {
        mExpandableListAdapter.replaceEvents(events);
    }

    @Override
    public void showEvents(final List<Event> events) {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mEmptyListView.setVisibility(View.GONE);
                mRefreshLayout.setRefreshing(false);
                mLoadingView.setVisibility(View.GONE);
                mExpandableListView.setVisibility(View.VISIBLE);
                addEvents(events);
            }
        };

        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showLoading() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mErrorListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mRefreshLayout.setRefreshing(true);
                mExpandableListView.setVisibility(View.INVISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showError() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mLoadingView.setVisibility(View.GONE);
                mExpandableListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showEmpty() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mLoadingView.setVisibility(View.GONE);
                mExpandableListView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void onToggleFavorite(int result) {
        String message;
        if (result == 0){
            message = "Added To Favorites!!!";
        }else {
            message = "Removed From Favorites!!!";
        }
        makeToast(message);
        //Intent myIntent = new Intent(getActivity(), EventsActivity.class);
        //startActivity(myIntent);

    }

    @Override
    public void toggleFavourite(String id) {
        mEventsPresenter.toggleFavourite(id);
    }

    private void makeToast(final String message){
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getActivity().getCurrentFocus(), message, Snackbar.LENGTH_LONG)
                        .show();
//                Toast.makeText(getActivity().getApplication().getApplicationContext(),
//                        message, Toast.LENGTH_SHORT).show();
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }
}
