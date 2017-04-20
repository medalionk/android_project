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

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.adapter.HomeAdapter;
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

    @Inject
    FragmentPresenter mFragmentPresenter;

    private HomeAdapter mAdapter;

    @Bind(R.id.loading)
    View mLoadingView;

    @Bind(R.id.error)
    View mErrorListView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

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
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mErrorListView.setVisibility(View.GONE);

                mLoadingView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showError() {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mLoadingView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.VISIBLE);
            }
        };
        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void showEmpty() {

    }

    public void addArticles(Collection<Article> articles) {
        mAdapter.replaceArticles(articles);
    }

    @Override
    public void showEvents(final List<Article> Article) {

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mLoadingView.setVisibility(View.GONE);
                mErrorListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                addArticles(Article);
            }
        };

        getActivity().runOnUiThread(myRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentPresenter.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentPresenter.onStart();
    }
}
