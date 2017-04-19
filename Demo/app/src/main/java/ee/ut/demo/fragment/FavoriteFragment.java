package ee.ut.demo.fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.adapter.FavoriteListener;
import ee.ut.demo.adapter.FavouriteAdapter;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerFavoriteEventsComponent;
import ee.ut.demo.injector.component.FavoriteEventsComponent;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.FavoriteEventsModule;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.presenter.FavouriteEventsPresenter;
import ee.ut.demo.mvp.view.FavouriteEventsView;

/**
 * Created by Bilal Abdullah on 4/18/2017.
 */

public class FavoriteFragment extends Fragment implements FavouriteEventsView, FavoriteListener {

    @Inject
    FavouriteEventsPresenter mEventPresenter;

    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;

    public FavoriteFragment() {

    }

    public static Fragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        mEventPresenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        injectDependencies();
        mEventPresenter.onCreate();

        ButterKnife.bind(this, rootView);

        initAdapter();
        initRecyclerView();
        initCollapsingToolbar(rootView);
        initPresenter();

        Picasso.with(getActivity())
                .load(R.drawable.header1)
                .into((ImageView) rootView.findViewById(R.id.backdrop));

        return rootView;
    }

    private  void injectDependencies() {
        ApplicationComponent appComponent = ((TartuApplication) getActivity().getApplication())
                .getApplicationComponent();
        FavoriteEventsComponent favouriteComponent = DaggerFavoriteEventsComponent.builder()
                .favoriteEventsModule(new FavoriteEventsModule())
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(appComponent)
                .build();

        favouriteComponent.inject(this);
    }

    private void initAdapter() {
        if (mAdapter == null) {
            mAdapter = new FavouriteAdapter(getActivity());
            mAdapter.setFavoriteListener(this);
        }
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new FavoriteFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mEventPresenter.attachView(this);
    }

    public void addEvents(Collection<Event> events) {
        mAdapter.replaceEvents(events);
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
    public void showFavouriteEvents(final List<Event> events) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                addEvents(events);
            }
        };

        getActivity().runOnUiThread(myRunnable);
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

    // source http://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/
    private void initCollapsingToolbar(View rootView) {

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

        AppBarLayout appBarLayout = (AppBarLayout) rootView.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.title_favourite));
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void toggleFavourite(String id) {
        mEventPresenter.toggleFavourite(id);
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

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
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics()));
    }
}
