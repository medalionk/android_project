package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.ScheduleActivity;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.FavoriteEventsModule;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Bilal Abdullah on 3/23/2017.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, FavoriteEventsModule.class})
public interface FavoriteEventsComponent {
    void inject(ScheduleActivity scheduleActivity);
    Context context();
}
