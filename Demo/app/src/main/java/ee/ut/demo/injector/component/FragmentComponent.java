package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.activity.EventsActivity;
import ee.ut.demo.fragment.EventFragment;
import ee.ut.demo.fragment.HomeFragment;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.EventsModule;
import ee.ut.demo.injector.module.FragmentModule;
import ee.ut.demo.injector.scope.PerActivity;

@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, FragmentModule.class})
public interface  FragmentComponent  {
    //void inject(EventFragment listFragment);
    void inject(HomeFragment homefragment);
    Context context();
}