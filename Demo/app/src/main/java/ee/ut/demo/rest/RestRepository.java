package ee.ut.demo.rest;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import ee.ut.demo.mvp.network.Repository;
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
}
