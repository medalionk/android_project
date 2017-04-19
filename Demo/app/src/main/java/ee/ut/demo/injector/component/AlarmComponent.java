package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.HomeActivity;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.AlarmModule;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Adewale Ayobami on 04/07/2017.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, AlarmModule.class})
public interface AlarmComponent {
    void inject(HomeActivity homeactivity);
    Context context();
}
