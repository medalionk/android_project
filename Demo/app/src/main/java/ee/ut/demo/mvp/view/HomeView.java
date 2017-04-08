package ee.ut.demo.mvp.view;

import java.util.List;

import ee.ut.demo.mvp.model.Event;

/**
 * Created by firars group on 4/7/2017.
 */
public interface HomeView extends View{
    void showFavouriteEvents(List<Event> events);
}
