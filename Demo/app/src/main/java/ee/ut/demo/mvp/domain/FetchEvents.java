package ee.ut.demo.mvp.domain;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.network.Repository;
import rx.Observable;

public class FetchEvents implements Usecase<List<Event>>{
    private Repository repository;
    private int mDate;

    public void setEventDate(int date){
        mDate = date;
    }

    public FetchEvents(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Event>> execute() {
        return repository.getEvents(mDate).map(new ResponseMappingFunc<List<Event>>());
    }
}
