package ee.ut.demo.injector.module;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.presenter.FavouriteEventsPresenter;
import ee.ut.demo.mvp.domain.repository.DatabaseRepository;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */
@Module(includes = DatabaseModule.class)
public class FavoriteEventsModule {

    @PerActivity
    @Provides
    public FavouriteEventsPresenter provideFavouriteEventsPresenter(DatabaseRepository dbRepo) {
        return new FavouriteEventsPresenter(dbRepo);
    }
}
