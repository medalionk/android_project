package ee.ut.demo.injector.component;

import dagger.Component;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.domain.database.EventTable;
import ee.ut.demo.injector.module.ActivityModule;
import ee.ut.demo.injector.module.DatabaseModule;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */
@PerActivity
@Component(dependencies = {ApplicationComponent.class},
        modules = {ActivityModule.class, DatabaseModule.class})
public interface DatabaseComponent {
    EventTable eventTable();
    Database database();
}
