package ee.ut.demo.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Element;
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
    public Observable<ResponseWrapper<Event>> getEvent(String eventId, String apiToken) {
        return apiService.getEvent(eventId, apiToken);
    }

    public Observable<ResponseWrapper<Event>> getHome(String eventId, String apiToken) {
        return apiService.getEvent(eventId, apiToken);
    }
    @Override
    public Observable<ResponseWrapper<List<Element>>> getElements(String pageId) {
        return apiService.getElements(pageId);
    }
    @Override
    public Observable<ResponseWrapper<List<Element>>> getHomeElements(String pageId) {
        return apiService.getHomeElements(pageId);
    }
}
