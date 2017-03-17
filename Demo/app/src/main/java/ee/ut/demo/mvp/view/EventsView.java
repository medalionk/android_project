package ee.ut.demo.mvp.view;

import java.util.List;

import ee.ut.demo.mvp.model.Event;

public interface EventsView extends View {
    void showEvents(List<Event> events);
    void showLoading();
    void showError();
}
