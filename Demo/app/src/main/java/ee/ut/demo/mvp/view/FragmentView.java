package ee.ut.demo.mvp.view;

import java.util.List;

import ee.ut.demo.mvp.model.Article;

/**
 * Created by firars group on 4/17/2017.
 */
public interface FragmentView extends View {
    void showEvents(List<Article> articles);
}
