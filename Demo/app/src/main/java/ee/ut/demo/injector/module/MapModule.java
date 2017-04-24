package ee.ut.demo.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.domain.repository.GoogleRepository;
import ee.ut.demo.injector.scope.PerActivity;
import ee.ut.demo.mvp.presenter.MapPresenter;

/**
 * Created by Bilal Abdullah on 4/22/2017.
 */
@Module(includes = DatabaseModule.class)
public class MapModule {

    @PerActivity
    @Provides
    public MapPresenter provideMapPresenter(Context context, GoogleRepository repository, Database database) {
        return new MapPresenter(context, repository, database);
    }
}
