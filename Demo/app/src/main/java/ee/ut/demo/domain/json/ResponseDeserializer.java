package ee.ut.demo.domain.json;


import com.google.android.gms.maps.model.LatLng;
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
import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;

public class ResponseDeserializer implements JsonDeserializer<ResponseWrapper> {

    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ResponseWrapper responseWrapper = new ResponseWrapper<>();

        if (json.isJsonObject() && json.getAsJsonObject().get("results") != null){
            JsonArray jsonArray = json.getAsJsonObject().get("results").getAsJsonArray();

            if(jsonArray.size() > 0){
                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                String id = Parse.getString(jsonObject.get("id"));
                JsonObject locationObject = jsonObject.get("geometry").getAsJsonObject()
                        .get("location").getAsJsonObject();
                double lat = Double.parseDouble(Parse.getString(locationObject.get("lat")));
                double lng = Double.parseDouble(Parse.getString(locationObject.get("lng")));
                LatLng location = new LatLng(lat, lng);
                String name = Parse.getString(jsonObject.get("name"));
                String placeId = Parse.getString(jsonObject.get("place_id"));
                String reference = Parse.getString(jsonObject.get("reference"));

                PlaceDetail placeDetail = new PlaceDetail.Builder().id(id)
                        .location(location).name(name)
                        .placeId(placeId).reference(reference).build();

                responseWrapper.body = placeDetail;
            }
        }
        else if(json.isJsonArray()){
            JsonArray jsonArray = json.getAsJsonArray();
            List<Element> elements = new ArrayList<>();

            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                String id = Parse.getString(jsonObject.get("id"));
                String updatedAt = Parse.getString(jsonObject.get("updated_at"));
                String title = Parse.getString(jsonObject.get("title"));

                elements.add(new Element(id, title, updatedAt, false));
            }

            responseWrapper.body = elements;
        }
        else if(json.getAsJsonObject().get("excerpt") != null){

            JsonObject jsonObject = json.getAsJsonObject();
            String id = Parse.getString(jsonObject.get("id"));
            String title = Parse.getString(jsonObject.get("title"));
            String excerpt = Parse.fromHtml(Parse.getString(jsonObject.get("excerpt"))).toString();
            String publicUrl = Parse.getString(jsonObject.get("public_url"));

            jsonObject = jsonObject.get("image").getAsJsonObject();
            String imageUrl = null;
            if(jsonObject != null && jsonObject.has("public_url")){
                imageUrl = Parse.getString(jsonObject.get("public_url"));
            }

            responseWrapper.body = new Article.Builder(title)
                    .id(id).excerpt(excerpt)
                    .imageUrl(imageUrl).publicUrl(publicUrl)
                    .build();
        }
        else {

            JsonObject jsonObject = json.getAsJsonObject();
            String id = Parse.getString(jsonObject.get("id"));
            String title = Parse.getString(jsonObject.get("title"));
            String updatedAt = Parse.getString(jsonObject.get("updated_at"));
            String public_url = Parse.getString(jsonObject.get("public_url"));

            jsonObject = jsonObject.get("values").getAsJsonObject();

            String startHr = Parse.getString(jsonObject.get("algusaegtund"));
            String startMin = Parse.getString(jsonObject.get("algusaegmin"));
            String endHr = Parse.getString(jsonObject.get("loppaegtund"));
            String endMin = Parse.getString(jsonObject.get("loppaegmin"));

            String date = Parse.getString(jsonObject.get("kuupaev"));
            String startTime = startHr + ":" + startMin;
            String endTime = endHr + ":" + endMin;
            String location = Parse.getString(jsonObject.get("koht"));
            String ticket = Parse.getString(jsonObject.get("hind"));
            String organizer = Parse.getString(jsonObject.get("korraldajad"));
            String additionalInfo = Parse.fromHtml(Parse.getString(jsonObject.get("korraldajanimi"))).toString();
            String imageUrl = Parse.getString(jsonObject.get("pilt"));
            String description = Parse.fromHtml(Parse.getString(jsonObject.get("kirjeldus"))).toString();

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
}
