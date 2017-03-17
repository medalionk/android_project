package ee.ut.demo.mvp.presenter;

import ee.ut.demo.mvp.view.View;

public interface Presenter<T extends View> {
    void onCreate();

    void onStart();

    void onStop();

    void onPause();

    void attachView(T view);
}
