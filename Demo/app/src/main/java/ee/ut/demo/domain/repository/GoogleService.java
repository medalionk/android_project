package ee.ut.demo.domain.repository;

import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 4/24/2017.
 */

public interface GoogleService {
    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Observable<ResponseWrapper<PlaceDetail>> mapPlaceTextSearch(@Query("query") String query,
                                                                @Query("key") String key);
}
