package ee.ut.demo.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

public interface Repository {
    Observable<ResponseWrapper<Event>> getEvent(String eventId, String apiToken);
    Observable<ResponseWrapper<List<Element>>> getElements(String pageId);
    Observable<ResponseWrapper<Event>> getHome(String eventId, String apiToken);
    Observable<ResponseWrapper<List<Element>>> getHomeElements(String pageId);
}
