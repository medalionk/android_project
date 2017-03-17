package ee.ut.demo.mvp.domain;

import rx.Observable;

public interface Usecase<T> {
    Observable<T> execute();
}
