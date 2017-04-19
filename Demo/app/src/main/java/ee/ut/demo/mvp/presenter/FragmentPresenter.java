package ee.ut.demo.mvp.presenter;

import android.content.Context;

import java.util.List;

import ee.ut.demo.domain.database.Database;
import ee.ut.demo.helpers.ConfigManager;
import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.domain.repository.ResponseMappingFunc;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.view.EventsView;
import ee.ut.demo.mvp.view.FragmentView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class FragmentPresenter implements Presenter<FragmentView>{

    private static final String PAGE_ID = "page_id";
    private static final String API_TOKEN = "api_token";

    private Subscription mGetEventsSubscription;
    private FragmentView mEventsView;
    private List<Event> mEvents;
    private Database mDatabase;
    private Repository mRepository;
    private Context mContext;
    private int mPage;


    public FragmentPresenter(Context context, Repository restRepo, Database database) {
        mContext = context;
        mRepository = restRepo;
        mDatabase = database;
    }

    @Override
    public void onCreate() {
        getEvents();
    }

    @Override
    public void onStart() {
        getEvents();
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
    public void attachView(FragmentView view) {
        this.mEventsView = view;
    }

    public void setPage(int page){
        mPage = page;
    }

    private void getEvents() {

        final String pageId = ConfigManager.getProperty(mContext, PAGE_ID);
        final String apiToken = ConfigManager.getProperty(mContext, API_TOKEN);

        mGetEventsSubscription = mRepository.getElements(pageId)
                .map(new ResponseMappingFunc<List<Element>>())
                .subscribeOn(Schedulers.io())
                .onErrorReturn(new Func1<Throwable, List<Element>>() {
                    @Override
                    public List<Element> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return null;
                    }
                })
                .flatMap(new Func1<List<Element>, Observable<List<Element>>>() {
                    @Override
                    public Observable<List<Element>> call(List<Element> elements) {
                        return mDatabase.checkUpdateEvents(elements)
                                .subscribeOn(Schedulers.computation())
                                .onErrorReturn(new Func1<Throwable, List<Element>>() {
                                    @Override
                                    public List<Element> call(Throwable throwable) {
                                        throwable.printStackTrace();
                                        return null;
                                    }
                                });
                    }
                })
                .flatMap(new Func1<List<Element>, Observable<Element>>() {
                    @Override
                    public Observable<Element> call(List<Element> elements) {
                        return Observable.from(elements);
                    }
                })
                .filter(new Func1<Element, Boolean>(){
                    public Boolean call(Element element){
                        return element.isShouldUpdate();
                    }
                })
                .flatMap(new Func1<Element, Observable<Event>>() {
                    @Override
                    public Observable<Event> call(Element element) {
                        return Observable.zip(Observable.just(element),
                                mRepository.getEvent(element.getId(), apiToken)
                                        .map(new ResponseMappingFunc<Event>())
                                        .subscribeOn(Schedulers.io())
                                        .onErrorReturn(new Func1<Throwable, Event>() {
                                            @Override
                                            public Event call(Throwable throwable) {
                                                throwable.printStackTrace();
                                                mEventsView.showError();
                                                return null;
                                            }
                                        }),
                                new Func2<Element, Event, Event>() {
                                    @Override
                                    public Event call(Element tvShow, Event event) {
                                        return event;
                                    }
                                });

                    }
                })
                .toList()
                .subscribe(new Action1<List<Event>>() {
                    @Override
                    public void call(List<Event> events) {
                        if (events != null && events.size() > 0){
                            addEvents(events);
                        }else {
                            getEventsByPage();
                        }
                    }
                });


    }

    void getEventsByPage(){

        if (mEvents != null) {
            mEventsView.showEvents(mEvents);
        } else {
            mEventsView.showLoading();
        }

        mGetEventsSubscription = mDatabase.getEventsByPage(mPage)
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
                        if (events != null && events.size() > 0) {
                            mEvents = events;
                            mEventsView.showEvents(events);
                        }else {
                            mEventsView.showEmpty();
                        }
                    }
                });
    }



    void addEvents(List<Event> events){
        mGetEventsSubscription = mDatabase.addEvents(events)
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
                        getEventsByPage();
                    }
                });
    }

    public void onRefresh() {
        mEvents = null;
        getEvents();
    }
}
