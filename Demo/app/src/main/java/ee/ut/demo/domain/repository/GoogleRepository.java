package ee.ut.demo.domain.repository;

import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

/**
 * Created by Bilal Abdullah on 4/24/2017.
 */

public interface GoogleRepository {
    Observable<ResponseWrapper<PlaceDetail>> mapPlaceTextSearch(String query, String key);
}
