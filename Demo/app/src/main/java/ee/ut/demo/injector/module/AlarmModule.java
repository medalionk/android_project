package ee.ut.demo.injector.module;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.presenter.AlarmPresenter;
import ee.ut.demo.mvp.presenter.FavouriteEventsPresenter;

/**
 * Created by Adewale Ayobami on 04/07/2017.
 */
@Module(includes = DatabaseModule.class)
public class AlarmModule {

    @PerActivity
    @Provides
    public AlarmPresenter provideFavouriteEventsPresenter(Database database) {
        return new AlarmPresenter(database);
    }
}
