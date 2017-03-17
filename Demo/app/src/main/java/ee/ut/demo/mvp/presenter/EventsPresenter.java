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

    public EventsPresenter(FetchEvents fetchEvents) {
        this.fetchEvents = fetchEvents;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

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

    @Override
    public void attachView(EventsView view) {
        this.eventsView = view;
        getEvents();
    }

    private void getEvents() {
        eventsView.showLoading();

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
                            eventsView.showEvents(events);
                        }
                    }
                });
    }

    public void onRefresh() {
        getEvents();
    }
}
