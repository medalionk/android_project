package ee.ut.demo.mvp.view;

import java.util.List;

import ee.ut.demo.mvp.model.Event;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */

public interface FavouriteEventsView extends View {
    void showFavouriteEvents(List<Event> events);
    void showLoading();
    void showError();
    void showEmpty();
}
