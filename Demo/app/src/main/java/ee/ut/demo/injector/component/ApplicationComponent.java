package ee.ut.demo.injector.component;

import android.app.Application;

import dagger.Component;
import ee.ut.demo.TartuApplication;
import ee.ut.demo.injector.module.ApplicationModule;
import ee.ut.demo.injector.module.NetworkModule;
import ee.ut.demo.injector.scope.PerApplication;
import ee.ut.demo.mvp.domain.repository.Repository;

@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
    Application application();
    TartuApplication tartuApplication();
    Repository repository();
}
