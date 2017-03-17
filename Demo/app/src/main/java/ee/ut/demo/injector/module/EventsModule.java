package ee.ut.demo.injector.module;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.domain.FetchEvents;
import ee.ut.demo.mvp.network.Repository;
import ee.ut.demo.mvp.presenter.EventsPresenter;

@Module
public class EventsModule {
    @PerActivity
    @Provides
    public FetchEvents provideGetEvents(Repository repository) {
        return new FetchEvents(repository);
    }

    @PerActivity
    @Provides
    public EventsPresenter provideEventsPresenter(FetchEvents fetchEvents) {
        return new EventsPresenter(fetchEvents);
    }
}
