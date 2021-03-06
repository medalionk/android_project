package ee.ut.demo.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;
import rx.Observable;

public interface Repository {
    Observable<ResponseWrapper<Event>> getEvent(String eventId, String apiToken);
    Observable<ResponseWrapper<List<Element>>> getElements(String path, String pageId,
                                                           String apiToken, String perPage);
    Observable<ResponseWrapper<Article>> getArticle(String articleId, String apiToken);


    Observable<ResponseWrapper<Event>> getHome(String eventId, String apiToken);
    Observable<ResponseWrapper<List<Element>>> getHomeElements(String pageId);

    Observable<ResponseWrapper<PlaceDetail>> mapPlaceTextSearch(String query, String key);
}

