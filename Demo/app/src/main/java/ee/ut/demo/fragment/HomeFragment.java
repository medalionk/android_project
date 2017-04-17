package ee.ut.demo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;

import ee.ut.demo.adapter.CustomExpandableListAdapter;
import ee.ut.demo.adapter.HomeAdapter;
import ee.ut.demo.adapter.HomeListener;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerFragmentComponent;
import ee.ut.demo.injector.component.FragmentComponent;
import ee.ut.demo.injector.module.FragmentModule;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.presenter.FragmentPresenter;
import ee.ut.demo.mvp.view.FragmentView;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class HomeFragment extends Fragment implements FragmentView, HomeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Inject
    FragmentPresenter mFragmentPresenter;

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;

    private static final String PAGE = "page_number";


    private int mPage = 0;



    public static HomeFragment newInstance(int page) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE, page);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE, mPage);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            injectDependencies();
            mFragmentPresenter.onCreate();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home, container, false);


        ButterKnife.bind(this, view);



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        initAdapter();
        initRecyclerView();


        initPresenter();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new HomeAdapter(getActivity());
            //mAdapter.setHomeListener(this);
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mFragmentPresenter.attachView(this);
        mFragmentPresenter.setPage(mPage);
    }

    private  void injectDependencies() {
        ApplicationComponent appComponent = ((TartuApplication) getActivity().getApplication())
                .getApplicationComponent();
        FragmentComponent fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule())
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(appComponent)
                .build();

        fragmentComponent.inject(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }

    public void addEvents(Collection<Event> events) {
        mAdapter.replaceEvents(events);
    }

    @Override
    public void showEvents(final List<Event> events) {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                addEvents(events);
            }
        };

        getActivity().runOnUiThread(myRunnable);
    }


    public void toggleHome(String id) {
        //mFragmentPresenter.toggleFavourite(id);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        ////
        //mEventsPresenter.setPage(mPage);
        mFragmentPresenter.onStart();
    }
}
