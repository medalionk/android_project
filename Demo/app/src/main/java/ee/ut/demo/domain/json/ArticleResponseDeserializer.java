package ee.ut.demo.domain.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.model.ResponseWrapper;

/**
 * Created by Bilal Abdullah on 4/20/2017.
 */

public class ArticleResponseDeserializer implements JsonDeserializer<ResponseWrapper> {

    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ResponseWrapper responseWrapper = new ResponseWrapper<>();

        JsonObject jsonObject = json.getAsJsonObject();
        String id = getString(jsonObject.get("id"));
        String title = getString(jsonObject.get("title"));
        String excerpt = getString(jsonObject.get("excerpt"));
        String publicUrl = getString(jsonObject.get("public_url"));

        jsonObject = jsonObject.get("image").getAsJsonObject();
        String imageUrl = getString(jsonObject.get("public_url"));

        Article article =  new Article.Builder(title)
                .id(id).excerpt(excerpt)
                .imageUrl(imageUrl).publicUrl(publicUrl)
                .build();

        responseWrapper.body = article;

        return responseWrapper;
    }

    private String getString(JsonElement element){
        return element.toString().replaceAll("^\"|\"$", "").replaceAll("(\\\\n)+", "");
    }
}
