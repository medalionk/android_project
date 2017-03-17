package ee.ut.demo.injector.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.injector.scope.PerActivity;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    public Context context() {
        return activity;
    }
}
