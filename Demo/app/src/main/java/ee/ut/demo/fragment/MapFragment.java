package ee.ut.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ee.ut.demo.R;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.component.DaggerMapComponent;
import ee.ut.demo.injector.component.MapComponent;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.MapModule;
import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.presenter.MapPresenter;
import ee.ut.demo.mvp.view.MapView;

/**
 * @Authors: Ayobami Adewale, Abdullah Bilal
 * @Supervisor Jakob Mass
 * @Project: Mobile Application Development Project (MTAT.03.183) Tartu Tudengipäevad Application
 * University of Tartu, Spring 2017.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MapView {

    private static final String PLACES_TAG = "places";
    private static float ZOOM_LEVEL = 13.5f;

    @Inject
    MapPresenter mMapPresenter;

    private GoogleMap mMap;
    private ArrayList<PlaceDetail> mPlaces = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMapPresenter.onStart();
    }


    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map, null, false);

        FragmentManager fm = getChildFragmentManager();
        ((SupportMapFragment)fm.findFragmentById(R.id.map)).getMapAsync(this);

        initPresenter();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mPlaces = savedInstanceState.getParcelableArrayList(PLACES_TAG);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PLACES_TAG, mPlaces);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        putMarkers(mPlaces);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapPresenter.onStop();
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

    @Override
    public void putMarkerOnMap(final List<PlaceDetail> places) {
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                mPlaces.clear();
                mPlaces.addAll(places);
                putMarkers(places);
            }
        };

        getActivity().runOnUiThread(myRunnable);
    }

    private void putMarkers(final List<PlaceDetail> places){

        if(places != null && places.size() > 0){
            PlaceDetail place = null;
            for (int i = 0; i < places.size(); i++) {
                place = places.get(i);
                if(place != null){
                    mMap.addMarker(new MarkerOptions().position(place.getLocation())
                            .title(place.getName()));
                }
            }

            if(place != null){
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        place.getLocation(), ZOOM_LEVEL));
            }

        }
    }

    private  void injectDependencies() {
        ApplicationComponent appComponent = ((TartuApplication) getActivity().getApplication())
                .getApplicationComponent();
        MapComponent mapComponent = DaggerMapComponent.builder()
                .mapModule(new MapModule())
                .activityModule(new ActivityModule(getActivity()))
                .applicationComponent(appComponent)
                .build();

        mapComponent.inject(this);
    }

    private void initPresenter() {
        mMapPresenter.attachView(this);
    }
}
