package ee.ut.demo.mvp.presenter;

import java.util.List;

import ee.ut.demo.domain.database.Database;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.view.FavouriteEventsView;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class FavouriteEventsPresenter implements Presenter<FavouriteEventsView>{

    private Subscription mGetFavouriteEventsSubscription;
    private FavouriteEventsView mFavouriteEventsView;
    private Database mDatabase;


    public FavouriteEventsPresenter(Database database) {
        mDatabase = database;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        getFavouriteEvents();
    }

    @Override
    public void onStop() {
        if (mGetFavouriteEventsSubscription != null && !mGetFavouriteEventsSubscription.isUnsubscribed()) {
            mGetFavouriteEventsSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(FavouriteEventsView view) {
        this.mFavouriteEventsView = view;
    }

    void getFavouriteEvents(){

        mFavouriteEventsView.showLoading();
        mGetFavouriteEventsSubscription = mDatabase.getFavouriteEvents()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Event>>() {
                    @Override
                    public List<Event> call(Throwable throwable) {
                        throwable.printStackTrace();
                        mFavouriteEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        if (events != null&& events.size() > 0) {
                            mFavouriteEventsView.showFavouriteEvents(events);
                        }else {
                            mFavouriteEventsView.showEmpty();
                        }
                    }
                });
    }

    public void toggleFavourite(String id){
        mGetFavouriteEventsSubscription = mDatabase.toggleFavourite(id)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        throwable.printStackTrace();
                        mFavouriteEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {
                        getFavouriteEvents();
                    }
                });
    }
}
