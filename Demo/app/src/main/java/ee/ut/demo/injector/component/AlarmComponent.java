package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.HomeActivity;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.AlarmModule;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, AlarmModule.class})
public interface AlarmComponent {
    void inject(HomeActivity homeactivity);
    Context context();
}
