package ee.ut.demo.injector.component;

import android.content.Context;

import dagger.Component;
import ee.ut.demo.fragment.MapFragment;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.MapModule;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Bilal Abdullah on 4/22/2017.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, MapModule.class})
public interface MapComponent {
    void inject(MapFragment mapFragment);
    Context context();
}
