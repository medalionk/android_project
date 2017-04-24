package ee.ut.demo.domain.repository;

import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 4/24/2017.
 */

public class GoogleMapRepository  implements GoogleRepository{
    private GoogleService googleService;

    public GoogleMapRepository(Retrofit retrofit) {
        googleService = retrofit.create(GoogleService.class);
    }

    @Override
    public Observable<ResponseWrapper<PlaceDetail>> mapPlaceTextSearch(String query, String key) {
        return googleService.mapPlaceTextSearch(query, key);
    }
}
