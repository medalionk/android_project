package ee.ut.demo.mvp.domain.repository;

import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.functions.Func1;

public class ResponseMappingFunc<R> implements Func1<ResponseWrapper<R>, R> {
    @Override
    public R call(ResponseWrapper<R> tResponseWrapper) {
        if (tResponseWrapper == null) {
            return null;
        }
        return tResponseWrapper.body;
    }
}
