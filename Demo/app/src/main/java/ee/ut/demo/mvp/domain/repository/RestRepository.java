package ee.ut.demo.mvp.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit.Retrofit;
import rx.Observable;

public class RestRepository implements Repository {

    private ApiService apiService;

    public RestRepository(Retrofit retrofit) {
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public Observable<ResponseWrapper<List<Event>>> getEvents() {
        return apiService.getEvents();
    }

    @Override
    public Observable<List<Event>> getEventsByPage(int page) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Observable<List<Event>> getFavouriteEvents() {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Observable<Integer> setFavourite(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Observable<Integer> unsetFavourite(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    @Override
    public Observable<Integer> addEvents(List<Event> events) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }
}
