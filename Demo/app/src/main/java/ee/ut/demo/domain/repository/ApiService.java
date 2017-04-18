package ee.ut.demo.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface  ApiService {

    @GET("elements")
    Observable<ResponseWrapper<List<Element>>> getElements(@Query("page_id") String pageId);

    @GET("elements/{event_id}")
    Observable<ResponseWrapper<Event>> getEvent(@Path("event_id") String eventId,
                                                       @Query("api_token") String apiToken);

    @GET("elements")
    Observable<ResponseWrapper<List<Element>>> getHomeElements(@Query("home_id") String pageId);

    @GET("elements/{event_id}")
    Observable<ResponseWrapper<Event>> getHome(@Path("home_id") String eventId,
                                                @Query("api_token") String apiToken);
}
