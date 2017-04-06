package ee.ut.demo.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.mvp.presenter.EventsPresenter;

@Module(includes = DatabaseModule.class)
public class EventsModule {

    @Provides
    public EventsPresenter provideEventsPresenter(Context context, Repository repository,
                                                  Database database) {
        return new EventsPresenter(context, repository, database);
    }
}
