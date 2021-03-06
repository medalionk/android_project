package ee.ut.demo.injector.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.domain.database.Database;
import ee.ut.demo.domain.database.DatabaseHandler;
import ee.ut.demo.domain.database.EventTable;
import ee.ut.demo.injector.scope.PerActivity;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */
@Module
public class DatabaseModule {

    @PerActivity
    @Provides
    public EventTable provideEventTable() {
        return new EventTable();
    }

    @PerActivity
    @Provides
    public Database provideDatabase(Context context, EventTable eventTable) {
        return new DatabaseHandler(context, eventTable);
    }

//    @PerActivity
//    @Provides
//    public DatabaseRepository provideDatabaseRepository(Database database) {
//        return new DatabaseRepository(database);
//    }
}
