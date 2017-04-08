package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.EventsActivity;
import ee.ut.demo.fragment.EventFragment;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.EventsModule;
import ee.ut.demo.injector.scope.PerActivity;

@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, EventsModule.class})
public interface  EventsComponent  {
    void inject(EventFragment listFragment);
    void inject(EventsActivity eventsActivity);
    Context context();
}