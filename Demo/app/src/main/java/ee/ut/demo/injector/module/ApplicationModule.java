package ee.ut.demo.injector.module;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.injector.scope.PerApplication;

@Module
public class ApplicationModule {
    private final TartuApplication application;

    public ApplicationModule(TartuApplication application) {
        this.application = application;
    }

    @Provides
    @PerApplication
    public TartuApplication provideMvpApplication() {
        return application;
    }

    @Provides
    @PerApplication
    public Application provideApplication() {
        return application;
    }
}
