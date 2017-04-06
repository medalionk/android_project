package ee.ut.demo.domain.database;

import java.util.List;

import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public interface Database {
    Observable<List<Event>> getEventsByPage(int page);
    Observable<List<Event>> getFavouriteEvents();
    Observable<Integer> toggleFavourite(String id);
    Observable<Integer> addEvents(List<Event> events);
    Observable<List<Element>> checkUpdateEvents(List<Element> elements);
}
