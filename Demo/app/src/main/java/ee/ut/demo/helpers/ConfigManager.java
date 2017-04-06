package ee.ut.demo.helpers;

import android.content.Context;

import java.util.Properties;

/**
 * Created by Bilal Abdullah on 4/4/2017.
 */

public class ConfigManager {
    private static final String PROPERTIES_FILE = "app.properties";

    public static String getProperty(Context context, String property) {
        PropertyReader propertyReader = new PropertyReader(context);
        Properties properties = propertyReader.getMyProperties(PROPERTIES_FILE);
        return properties.getProperty(property);
    }
}
