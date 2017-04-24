package ee.ut.demo.mvp.presenter;

import android.content.Context;

import java.util.List;

import ee.ut.demo.domain.database.Database;
import ee.ut.demo.domain.repository.GoogleRepository;
import ee.ut.demo.domain.repository.ResponseMappingFunc;
import ee.ut.demo.helpers.ConfigManager;
import ee.ut.demo.helpers.Connection;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.view.MapView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal Abdullah on 4/20/2017.
 */

public class MapPresenter implements Presenter<MapView>{

    private static final String API_KEY = "google_place_api_key";
    private Subscription mSubscription;
    private MapView mMapView;
    private Database mDatabase;
    private GoogleRepository mRepository;
    private Context mContext;
    private List<Event> mEvents;

    public MapPresenter(Context context, GoogleRepository restRepo, Database database) {
        mContext = context;
        mRepository = restRepo;
        mDatabase = database;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        getMapPlaces();
    }

    @Override
    public void onStop() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(MapView view) {
        this.mMapView = view;
    }

    private void getMapPlaces(){

        if(Connection.isInternetAvailable(mContext)) {
            try {

                final String apiKey = ConfigManager.getProperty(mContext, API_KEY);
                mSubscription = mDatabase.getFavouriteEvents()
                        .subscribeOn(Schedulers.io())
                        .onErrorReturn(new Func1<Throwable, List<Event>>() {
                            @Override
                            public List<Event> call(Throwable throwable) {
                                throwable.printStackTrace();
                                return null;
                            }
                        })
                        .flatMap(new Func1<List<Event>, Observable<Event>>() {
                            @Override
                            public Observable<Event> call(List<Event> events) {
                                return Observable.from(events);
                            }
                        })
                        .flatMap(new Func1<Event, Observable<PlaceDetail>>() {
                            @Override
                            public Observable<PlaceDetail> call(Event event) {
                                final String query = event.getLocation() +
                                        ", Tartu, Estonia";
                                return Observable.zip(Observable.just(event),
                                        mRepository.mapPlaceTextSearch(query, apiKey)
                                                .map(new ResponseMappingFunc<PlaceDetail>())
                                                .subscribeOn(Schedulers.io())
                                                .onErrorReturn(new Func1<Throwable, PlaceDetail>() {
                                                    @Override
                                                    public PlaceDetail call(Throwable throwable) {
                                                        throwable.printStackTrace();
                                                        return null;
                                                    }
                                                }),
                                        new Func2<Event, PlaceDetail, PlaceDetail>() {
                                            @Override
                                            public PlaceDetail call(Event event1, PlaceDetail placeDetail) {
                                                return placeDetail;
                                            }
                                        });
                            }
                        })
                        .toList()
                        .onErrorReturn(new Func1<Throwable, List<PlaceDetail>>() {
                            @Override
                            public List<PlaceDetail> call(Throwable throwable) {
                                throwable.printStackTrace();

                                return null;
                            }
                        })
                        .subscribe(new Action1<List<PlaceDetail>>() {
                            @Override
                            public void call(List<PlaceDetail> details) {
                                if (details != null && details.size() > 0){
                                    mMapView.putMarkerOnMap(details);
                                }
                            }
                        });
            }catch (Exception ignored){}
        }
    }
}
