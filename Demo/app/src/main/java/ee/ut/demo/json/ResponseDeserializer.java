package ee.ut.demo.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ee.ut.demo.data.Data;
import ee.ut.demo.injector.module.NetworkModule;
import ee.ut.demo.mvp.model.Event;
import ee.ut.demo.mvp.model.ResponseWrapper;

public class ResponseDeserializer implements JsonDeserializer<ResponseWrapper> {

    private Type eventType = new TypeToken<Event>() {
    }.getType();

    private Gson gson = new GsonBuilder()
            .setDateFormat(NetworkModule.API_DATE_FORMAT)
            .setFieldNamingPolicy(NetworkModule.API_JSON_NAMING_POLICY)
            .create();

    @Override
    public ResponseWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        ResponseWrapper responseWrapper = null;

        List<Event> events = new ArrayList<>();
        events.addAll(Data.getDummyData());

        responseWrapper = new ResponseWrapper<>();
        responseWrapper.body = events;

        return responseWrapper;
    }

    private Event parseEvent(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, eventType);
    }
}
