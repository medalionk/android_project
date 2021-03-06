package ee.ut.demo.injector.module;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.presenter.FavouriteEventsPresenter;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */
@Module(includes = DatabaseModule.class)
public class FavoriteEventsModule {

    @PerActivity
    @Provides
    public FavouriteEventsPresenter provideFavouriteEventsPresenter(Database database) {
        return new FavouriteEventsPresenter(database);
    }
}
