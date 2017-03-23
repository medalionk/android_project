package ee.ut.demo.injector.module;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.domain.repository.DatabaseRepository;
import ee.ut.demo.mvp.domain.repository.Repository;
import ee.ut.demo.mvp.presenter.EventsPresenter;

@Module(includes = DatabaseModule.class)
public class EventsModule {

    @PerActivity
    @Provides
    public EventsPresenter provideEventsPresenter(Repository repository,
                                                  DatabaseRepository dbRepo) {
        return new EventsPresenter(repository, dbRepo);
    }
}
