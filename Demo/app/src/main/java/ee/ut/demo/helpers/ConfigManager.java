package ee.ut.demo.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Properties;

import ee.ut.demo.mvp.model.PageType;

/**
 * Created by Bilal Abdullah on 4/4/2017.
 */

public class ConfigManager {

    private static final String ENG_ARTICLE_PAGE = "eng_article_page_id";
    private static final String EST_ARTICLE_PAGE = "est_article_page_id";
    private static final String ENG_EVENT_PAGE = "eng_event_page_id";
    private static final String EST_EVENT_PAGE = "est_event_page_id";
    private static final String PROPERTIES_FILE = "app.properties";
    private static final String EST_IDX = "0";
    private static final String ENG_IDX = "1";

    public static String getProperty(Context context, String property) {
        PropertyReader propertyReader = new PropertyReader(context);
        Properties properties = propertyReader.getMyProperties(PROPERTIES_FILE);
        return properties.getProperty(property);
    }

    public static String getPageID(Context context, PageType pageType) {

        String language = getPreferredLanguage(context);
        String property;
        if(pageType == PageType.ARTICLE) property = getArticlePropertyName(language);
        else property = getEventPropertyName(language);

        PropertyReader propertyReader = new PropertyReader(context);
        Properties properties = propertyReader.getMyProperties(PROPERTIES_FILE);
        return properties.getProperty(property);
    }

    private static String getArticlePropertyName(String language){
        if(language.equals(EST_IDX)) return EST_ARTICLE_PAGE;
        else return ENG_ARTICLE_PAGE;
    }

    private static String getEventPropertyName(String language){
        if(language.equals(EST_IDX)) return EST_EVENT_PAGE;
        else return ENG_EVENT_PAGE;
    }

    private static String getPreferredLanguage(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("event_language", "");
    }
}
