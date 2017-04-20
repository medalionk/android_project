package ee.ut.demo.mvp.presenter;

import android.content.Context;

import java.util.List;

import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.domain.repository.ResponseMappingFunc;
import ee.ut.demo.helpers.ConfigManager;
import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.view.FragmentView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class FragmentPresenter implements Presenter<FragmentView>{

    private static final String ARTICLE_PAGE_ID = "article_page_id";
    private static final String API_TOKEN = "api_token";
    private static final String ARTICLE_PATH = "articles";

    private Subscription mGetEventsSubscription;
    private FragmentView mFragmentView;
    private List<Article> mArticles;
    private Repository mRepository;
    private Context mContext;

    public FragmentPresenter(Context context, Repository restRepo) {
        mContext = context;
        mRepository = restRepo;
    }

    @Override
    public void onCreate() {
        getArticles();
    }

    @Override
    public void onStart() {
        getArticles();
    }

    @Override
    public void onStop() {
        if (mGetEventsSubscription != null && !mGetEventsSubscription.isUnsubscribed()) {
            mGetEventsSubscription.unsubscribe();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(FragmentView view) {
        this.mFragmentView = view;
    }

    private void getArticles() {

        if (mArticles != null) {
            mFragmentView.showEvents(mArticles);
        } else {
            mFragmentView.showLoading();
        }

        final String pageId = ConfigManager.getProperty(mContext, ARTICLE_PAGE_ID);
        final String apiToken = ConfigManager.getProperty(mContext, API_TOKEN);

        mGetEventsSubscription = mRepository.getElements(ARTICLE_PATH, pageId, apiToken)
                .map(new ResponseMappingFunc<List<Element>>())
                .subscribeOn(Schedulers.computation())
                .onErrorReturn(new Func1<Throwable, List<Element>>() {
                    @Override
                    public List<Element> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return null;
                    }
                })

                .flatMap(new Func1<List<Element>, Observable<Element>>() {
                    @Override
                    public Observable<Element> call(List<Element> elements) {
                        return Observable.from(elements)
                                .subscribeOn(Schedulers.computation())
                                .onErrorReturn(new Func1<Throwable, Element>() {
                                    @Override
                                    public Element call(Throwable throwable) {
                                        throwable.printStackTrace();
                                        return null;
                                    }
                                });
                    }
                })

                .flatMap(new Func1<Element, Observable<Article>>() {
                    @Override
                    public Observable<Article> call(Element element) {
                        return Observable.zip(Observable.just(element),
                                mRepository.getArticle(element.getId(), apiToken)
                                        .map(new ResponseMappingFunc<Article>())
                                        .subscribeOn(Schedulers.io())
                                        .onErrorReturn(new Func1<Throwable, Article>() {
                                            @Override
                                            public Article call(Throwable throwable) {
                                                throwable.printStackTrace();
                                                mFragmentView.showError();
                                                return null;
                                            }
                                        }),
                                new Func2<Element, Article, Article>() {
                                    @Override
                                    public Article call(Element tvShow, Article article) {
                                        return article;
                                    }
                                });

                    }
                })
                .toList()
                .subscribe(new Action1<List<Article>>() {
                    @Override
                    public void call(List<Article> articles) {
                        if (articles != null && articles.size() > 0) {
                            mArticles = articles;
                            mFragmentView.showEvents(articles);
                        }else {
                            mFragmentView.showEmpty();
                        }
                    }
                });


    }

    public void onRefresh() {
        mArticles = null;
        getArticles();
    }
}
