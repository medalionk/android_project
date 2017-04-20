package ee.ut.demo.domain.json;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ee.ut.demo.helpers.Parse;
import ee.ut.demo.mvp.model.Article;
import ee.ut.demo.mvp.model.Details;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;

public class ResponseDeserializer implements JsonDeserializer<ResponseWrapper> {

    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ResponseWrapper responseWrapper = new ResponseWrapper<>();

        if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            List<Element> elements = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                String id = getString(jsonObject.get("id"));
                String updatedAt = getString(jsonObject.get("updated_at"));
                String title = getString(jsonObject.get("title"));

                elements.add(new Element(id, title, updatedAt, false));
            }

            responseWrapper.body = elements;
        }
        else if(json.getAsJsonObject().get("excerpt") != null){

            JsonObject jsonObject = json.getAsJsonObject();
            String id = getString(jsonObject.get("id"));
            String title = getString(jsonObject.get("title"));
            String excerpt = Parse.fromHtml(getString(jsonObject.get("excerpt"))).toString();
            String publicUrl = getString(jsonObject.get("public_url"));

            jsonObject = jsonObject.get("image").getAsJsonObject();
            String imageUrl = null;
            if(jsonObject != null && jsonObject.has("public_url")){
                imageUrl = getString(jsonObject.get("public_url"));
            }

            responseWrapper.body = new Article.Builder(title)
                    .id(id).excerpt(excerpt)
                    .imageUrl(imageUrl).publicUrl(publicUrl)
                    .build();
        }
        else {

            JsonObject jsonObject = json.getAsJsonObject();
            String id = getString(jsonObject.get("id"));
            String title = getString(jsonObject.get("title"));
            String updatedAt = getString(jsonObject.get("updated_at"));
            String public_url = getString(jsonObject.get("public_url"));

            jsonObject = jsonObject.get("values").getAsJsonObject();

            String startHr = getString(jsonObject.get("algusaegtund"));
            String startMin = getString(jsonObject.get("algusaegmin"));
            String endHr = getString(jsonObject.get("loppaegtund"));
            String endMin = getString(jsonObject.get("loppaegmin"));

            String date = getString(jsonObject.get("kuupaev"));
            String startTime = startHr + ":" + startMin;
            String endTime = endHr + ":" + endMin;
            String location = getString(jsonObject.get("koht"));
            String ticket = getString(jsonObject.get("hind"));
            String organizer = getString(jsonObject.get("korraldajad"));
            String additionalInfo = Parse.fromHtml(getString(jsonObject.get("korraldajanimi"))).toString();
            String imageUrl = getString(jsonObject.get("pilt"));
            String description = Parse.fromHtml(getString(jsonObject.get("kirjeldus"))).toString();

            Event event =  new Event.Builder(title)
                    .id(id)
                    .startTime(startTime)
                    .endTime(endTime)
                    .location(location)
                    .updatedAt(updatedAt)
                    .details(
                            new Details.Builder()
                            .description(description)
                            .organizer(organizer)
                            .additionalInfo(additionalInfo)
                            .songBook("")
                            .imageUrl(imageUrl)
                            .ticket(ticket)
                            .publicUrl(public_url)
                            .date(date)
                            .build())
                    .build();

            responseWrapper.body = event;
        }

        return responseWrapper;
    }

    private String getString(JsonElement element){
        return element.toString().replaceAll("^\"|\"$", "").replaceAll("(\\\\n)+", "");
    }

}
