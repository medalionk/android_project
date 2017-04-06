package ee.ut.demo.helpers;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Bilal Abdullah on 4/4/2017.
 */

public class PropertyReader {
    private Context context;
    private Properties properties;

    public PropertyReader(Context context){
        this.context=context;
        properties = new Properties();
    }

    public Properties getMyProperties(String file){
        try{
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(file);
            properties.load(inputStream);

        }catch (Exception e){

        }

        return properties;
    }
}
