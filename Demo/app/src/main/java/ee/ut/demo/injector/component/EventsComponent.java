package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.EventsActivity;
import ee.ut.demo.activity.ScheduleActivity;
import ee.ut.demo.fragment.ExpandableListFragment;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.EventsModule;
import ee.ut.demo.injector.scope.PerActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, EventsModule.class})
public interface EventsComponent  {

    void inject(EventsActivity eventsActivity);
    void inject(ExpandableListFragment listFragment);
    Context context();
}