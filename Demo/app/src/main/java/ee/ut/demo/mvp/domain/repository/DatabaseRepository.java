package ee.ut.demo.mvp.domain.repository;

import java.util.List;

import ee.ut.demo.database.Database;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public class DatabaseRepository implements Repository {

    private Database mDatabase;

    public DatabaseRepository(Database database) {
        mDatabase = database;
    }

    @Override
    public Observable<ResponseWrapper<List<Event>>> getEvents() {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Observable<List<Event>> getEventsByPage(int page){
        return mDatabase.getEvents(page);
    }

    @Override
    public Observable<List<Event>> getFavouriteEvents(){
        return mDatabase.getFavouriteEvents();
    }

    @Override
    public Observable<Integer> setFavourite(int id){
        return mDatabase.setFavourite(id);
    }

    @Override
    public Observable<Integer> unsetFavourite(int id){
        return mDatabase.unsetFavourite(id);
    }

    @Override
    public Observable<Integer> addEvents(List<Event> events){
        return mDatabase.addEvents(events);
    }
}
