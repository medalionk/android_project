package ee.ut.demo.mvp.view;

import java.util.List;

import ee.ut.demo.mvp.model.PlaceDetail;

/**
 * Created by Bilal Abdullah on 4/20/2017.
 */

public interface MapView extends View{
    void putMarkerOnMap(final List<PlaceDetail> places);
}
