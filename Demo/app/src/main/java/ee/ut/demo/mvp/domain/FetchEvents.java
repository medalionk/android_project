package ee.ut.demo.mvp.domain;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.network.Repository;
import rx.Observable;

public class FetchEvents implements Usecase<List<Event>>{
    private Repository repository;

    public FetchEvents(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Event>> execute() {
        return repository.getEvents().map(new ResponseMappingFunc<List<Event>>());
    }
}
