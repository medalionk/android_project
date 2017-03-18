package ee.ut.demo.rest;

import java.util.List;

import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit.http.GET;
import rx.Observable;

public interface  ApiService {

    @GET("articles")
    Observable<ResponseWrapper<List<Event>>> getEvents();
}
