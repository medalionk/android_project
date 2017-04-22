package ee.ut.demo.mvp.presenter;

import java.util.List;

import ee.ut.demo.domain.database.Database;
import ee.ut.demo.helpers.Message;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.view.HomeView;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public class AlarmPresenter implements Presenter<HomeView>{

    private Subscription mSubscription;
    private HomeView mHomeView;
    private Database mDatabase;

    public AlarmPresenter(Database database) {
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
        if (mSubscription != null &&
                !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(HomeView view) {
        this.mHomeView = view;
    }

    private void getFavouriteEvents(){

        mSubscription = mDatabase.getFavouriteEvents()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Event>>() {
                    @Override
                    public List<Event> call(Throwable throwable) {
                        throwable.printStackTrace();
                        mHomeView.showError(Message.ERR_MSG_DB);
                        return null;
                    }
                })
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        mHomeView.addFavouriteEvents(events);
                    }
                });
    }
}
