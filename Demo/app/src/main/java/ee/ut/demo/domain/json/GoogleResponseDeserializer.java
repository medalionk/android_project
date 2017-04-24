package ee.ut.demo.domain.json;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ee.ut.demo.helpers.Parse;
import ee.ut.demo.mvp.model.PlaceDetail;
import ee.ut.demo.mvp.model.ResponseWrapper;

/**
 * Created by Bilal Abdullah on 4/24/2017.
 */

public class GoogleResponseDeserializer implements JsonDeserializer<ResponseWrapper> {
    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ResponseWrapper responseWrapper = new ResponseWrapper<>();
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
        return null;
    }
}
