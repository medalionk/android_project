package ee.ut.demo.mvp.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

public interface Repository {
    Observable<ResponseWrapper<List<Event>>> getEvents();
    Observable<List<Event>> getEventsByPage(int page);
    Observable<List<Event>> getFavouriteEvents();
    Observable<Integer> setFavourite(int id);
    Observable<Integer> unsetFavourite(int id);
    Observable<Integer> addEvents(List<Event> events);
}
