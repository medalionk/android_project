package ee.ut.demo.mvp.presenter;

import java.util.List;

import ee.ut.demo.mvp.domain.repository.DatabaseRepository;
import ee.ut.demo.mvp.domain.repository.Repository;
import ee.ut.demo.mvp.domain.repository.ResponseMappingFunc;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.view.EventsView;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EventsPresenter implements Presenter<EventsView>{

    private Subscription mGetEventsSubscription;
    private EventsView mEventsView;
    private List<Event> mEvents;
    private DatabaseRepository mDbRepo;
    private Repository mRestRepo;
    private int mPage;


    public EventsPresenter(Repository restRepo, DatabaseRepository dbRepo) {
        mRestRepo = restRepo;
        mDbRepo = dbRepo;
    }

    @Override
    public void onCreate() {
        getEvents();
    }

    @Override
    public void onStart() {
        getEventsByPage(0);
    }

    @Override
    public void onStop() {
        if (mGetEventsSubscription != null && !mGetEventsSubscription.isUnsubscribed()) {
            mGetEventsSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(EventsView view) {
        this.mEventsView = view;
    }

    public void setPage(int page){
        mPage = page;
    }

    private void getEvents() {

        //mEventsView.showLoading();
        mGetEventsSubscription = mRestRepo.getEvents().map(new ResponseMappingFunc<List<Event>>())
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Event>>() {
                    @Override
                    public List<Event> call(Throwable throwable) {
                        throwable.printStackTrace();
                        mEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        addEvents(events);
                        // TODO rewrite
                        if (events != null && events.size() > 0) {
                            mEvents = events;
                            mEventsView.showEvents(events);
                        }else {
                            mEventsView.showEmpty();
                        }
                    }
                });
    }

    void getEventsByPage(int page){

        mEventsView.showLoading();
        mGetEventsSubscription = mDbRepo.getEventsByPage(page)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Event>>() {
                    @Override
                    public List<Event> call(Throwable throwable) {
                        throwable.printStackTrace();
                        mEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        if (events != null&& events.size() > 0) {
                            mEvents = events;
                            mEventsView.showEvents(events);
                        }else {
                            mEventsView.showEmpty();
                        }
                    }
                });
    }

    public void setFavourite(int id){
        mGetEventsSubscription = mDbRepo.setFavourite(id)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        throwable.printStackTrace();
                        mEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {

                    }
                });
    }

    public void unSetFavourite(int id){
        mGetEventsSubscription = mDbRepo.unsetFavourite(id)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        throwable.printStackTrace();
                        mEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {
                        // TODO rewrite
                    }
                });
    }

    void addEvents(List<Event> events){
        mGetEventsSubscription = mDbRepo.addEvents(events)
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        throwable.printStackTrace();
                        mEventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer result) {

                    }
                });
    }

    public void onRefresh() {
        mEvents = null;
        getEventsByPage(0);
    }
}
