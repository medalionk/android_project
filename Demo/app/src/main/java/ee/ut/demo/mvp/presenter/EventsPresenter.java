package ee.ut.demo.mvp.presenter;

import java.util.List;

import ee.ut.demo.mvp.domain.FetchEvents;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.view.EventsView;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class EventsPresenter implements Presenter<EventsView>{

    private Subscription getEventsSubscription;
    private EventsView eventsView;
    private FetchEvents fetchEvents;
    private List<Event> mEvents;
    private int mDate;

    public EventsPresenter(FetchEvents fetchEvents) {
        this.fetchEvents = fetchEvents;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        getEvents();
    }

    @Override
    public void onStop() {
        if (getEventsSubscription != null && !getEventsSubscription.isUnsubscribed()) {
            getEventsSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    public void setEventDate(int date){
        mDate = date;
    }

    @Override
    public void attachView(EventsView view) {
        this.eventsView = view;
    }

    private void getEvents() {
        if (mEvents != null && mEvents.size() > 0) {
            eventsView.showEvents(mEvents);
        } else {
            eventsView.showLoading();
        }

        fetchEvents.setEventDate(mDate);
        getEventsSubscription = fetchEvents.execute()
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Event>>() {
                    @Override
                    public List<Event> call(Throwable throwable) {
                        throwable.printStackTrace();
                        eventsView.showError();
                        return null;
                    }
                })
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        if (events != null) {
                            if (events != null && events.size() > 0) {
                                mEvents = events;
                                eventsView.showEvents(events);
                            } else {
                                eventsView.showEmpty();
                            }
                        }
                    }
                });
    }

    public void onRefresh() {
        mEvents = null;
        getEvents();
    }
}
