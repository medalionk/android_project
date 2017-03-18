package ee.ut.demo;

import android.app.Application;

import ee.ut.demo.injector.component.ApplicationComponent;
import ee.ut.demo.injector.module.ApplicationModule;
import ee.ut.demo.injector.component.DaggerApplicationComponent;

public class TartuApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupInjector();
    }

    private void setupInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
