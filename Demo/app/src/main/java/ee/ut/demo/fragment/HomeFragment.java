package ee.ut.demo.fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.adapter.HomeAdapter;
import ee.ut.demo.helpers.Connection;
import ee.ut.demo.helpers.Message;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerFragmentComponent;
import ee.ut.demo.injector.component.FragmentComponent;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.FragmentModule;
import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.presenter.FragmentPresenter;
import ee.ut.demo.mvp.view.FragmentView;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class HomeFragment extends Fragment implements FragmentView {

    private static final String ARTICLES_TAG = "articles";

    @Inject
    FragmentPresenter mFragmentPresenter;

    @Bind(R.id.loading)
    View mLoadingView;

    @Bind(R.id.error)
    View mErrorListView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.empty_list)
    View mEmptyListView;

    private HomeAdapter mAdapter;
    private ArrayList<Article> mArticles = new ArrayList<>();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            injectDependencies();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mArticles = savedInstanceState.getParcelableArrayList(ARTICLES_TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTICLES_TAG, mArticles);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Connection.isInternetAvailable(getContext())) mFragmentPresenter.onStart();
        else showError(Message.ERR_MSG_NO_INTERNET);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        addArticles(mArticles);
    }

    @Override
    public void showLoading() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mErrorListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showError(String msg) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
        makeToast(msg);
    }

    @Override
    public void showEmpty() {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showEvents(final List<Article> articles) {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mEmptyListView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mArticles.clear();
                mArticles.addAll(articles);
                addArticles(articles);
            }
        };

        getActivity().runOnUiThread(myRunnable);
    }

    public void addArticles(Collection<Article> articles) {
        mAdapter.replaceArticles(articles);
    }

    private void makeToast(final String message){
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplication().getApplicationContext(),
                        message, Toast.LENGTH_LONG).show();
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
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
}
