package ee.ut.demo.domain.repository;

import java.util.List;

import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface  ApiService {

    @GET("{path}")
    Observable<ResponseWrapper<List<Element>>> getElements(@Path("path") String path,
            @Query("page_id") String pageId, @Query("api_token") String apiToken,
                                                           @Query("per_page") String perPage);

    @GET("elements/{event_id}")
    Observable<ResponseWrapper<Event>> getEvent(@Path("event_id") String eventId,
                                                @Query("api_token") String apiToken);

    @GET("elements")
    Observable<ResponseWrapper<List<Element>>> getHomeElements(@Query("home_id") String pageId);


    @GET("articles")
    Observable<ResponseWrapper<List<Element>>> getArticleElements(@Query("page_id") String pageId);
    @GET("articles/{article_id}")
    Observable<ResponseWrapper<Article>> getArticle(@Path("article_id") String articleId,
                                                    @Query("api_token") String apiToken);
}
