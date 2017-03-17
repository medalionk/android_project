package ee.ut.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.activity.EventsActivity;
import ee.ut.demo.adapter.CustomExpandableListAdapter;
import ee.ut.demo.data.ExpandableListDataPump;
import ee.ut.demo.injector.component.EventsComponent;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.presenter.EventsPresenter;
import ee.ut.demo.mvp.view.EventsView;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class ExpandableListFragment extends Fragment implements EventsView {

    public static final String ARG_OBJECT = "object";

    @Inject
    EventsPresenter eventsPresenter;

    @Bind(R.id.fc_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.fc_loading)
    View loadingView;

    @Bind(R.id.expandable_list)
    ExpandableListView mExpandableListView;


    private CustomExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;
    private HashMap<String, List<String>> mExpandableListDetail;

    private List<Event> mEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        eventsPresenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expandable_list, container, false);
        ButterKnife.bind(this, rootView);

        initAdapter();
        initView();

        initializePresenter();
        setupRefreshLayout();

        //mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandable_list);
        //mExpandableListDetail = ExpandableListDataPump.getData();
        //mExpandableListTitle = new ArrayList<>(mExpandableListDetail.keySet());
        /*mExpandableListAdapter = new CustomExpandableListAdapter(rootView.getContext(),
                mExpandableListTitle, mExpandableListDetail);
        mExpandableListView.setAdapter(mExpandableListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                int index = parent.getFlatListPosition(ExpandableListView
                        .getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                return false;
            }
        });*/

        return rootView;
    }

    private  void injectDependencies() {
        if (getActivity() instanceof EventsActivity) {
            EventsActivity activity = (EventsActivity) getActivity();
            EventsComponent registrationComponent = activity.getEventsComponent();
            registrationComponent.inject(this);
        }
    }

    private void initAdapter() {
        if (mExpandableListAdapter == null) {
            mExpandableListAdapter = new CustomExpandableListAdapter(getActivity());
        }
    }

    private void initView() {
        mExpandableListView.setAdapter(mExpandableListAdapter);
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

    private void initializePresenter() {
        eventsPresenter.attachView(this);
    }

    private void setupRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventsPresenter.onRefresh();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        eventsPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventsPresenter.onStart();
    }

    public void addEvent(Event event) {
        mExpandableListAdapter.add(event);
    }

    public void addEvents(Collection<Event> events) {
        mExpandableListAdapter.addAll(events);
    }

    @Override
    public void showLoading() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                loadingView.setVisibility(View.VISIBLE);
                refreshLayout.setRefreshing(true);
                mExpandableListView.setVisibility(View.INVISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showEvents(final List<Event> events) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                loadingView.setVisibility(View.GONE);
                mExpandableListView.setVisibility(View.VISIBLE);
                addEvents(events);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showError() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), R.string.error_loading, Toast.LENGTH_LONG).show();
            }
        };
        getActivity().runOnUiThread(myRunnable);

    }
}
