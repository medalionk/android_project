package ee.ut.demo.mvp.network;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

public interface Repository {
    Observable<ResponseWrapper<List<Event>>> getEvents(int date);
}
