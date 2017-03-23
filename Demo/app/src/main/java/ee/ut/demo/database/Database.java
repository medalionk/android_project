package ee.ut.demo.database;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public interface Database {
    Observable<List<Event>> getEvents(int page);
    Observable<List<Event>> getFavouriteEvents();
    Observable<Integer> setFavourite(int id);
    Observable<Integer> unsetFavourite(int id);
    Observable<Integer> addEvents(List<Event> events);
}
