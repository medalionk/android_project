package ee.ut.demo.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.mvp.presenter.FragmentPresenter;

@Module(includes = DatabaseModule.class)
public class FragmentModule {

    @Provides
    public FragmentPresenter provideEventsPresenter(Context context, Repository repository) {
        return new FragmentPresenter(context, repository);
    }
}
