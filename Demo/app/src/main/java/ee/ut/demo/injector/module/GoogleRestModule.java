package ee.ut.demo.injector.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.BuildConfig;
import ee.ut.demo.domain.json.GoogleResponseDeserializer;
import ee.ut.demo.domain.json.JsonExclusionStrategy;
import ee.ut.demo.domain.repository.GoogleMapRepository;
import ee.ut.demo.domain.repository.GoogleRepository;
import ee.ut.demo.helpers.Message;
import ee.ut.demo.injector.scope.PerApplication;
import ee.ut.demo.mvp.model.ResponseWrapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Bilal Abdullah on 4/24/2017.
 */

@Module
public class GoogleRestModule {

    @Provides
    @PerApplication
    GoogleRepository provideGoogleMapRepository(@Named("googleRetrofit") Retrofit retrofit) {
        return new GoogleMapRepository(retrofit);
    }

    @Provides
    @PerApplication
    @Named("googleRetrofit")
    Retrofit provideRetrofit() {
        String endpointUrl = BuildConfig.googleMapEndpointUrl;
        Gson gson = new GsonBuilder()
                .setDateFormat(Message.API_DATE_FORMAT)
                .setFieldNamingPolicy(Message.API_JSON_NAMING_POLICY)
                .registerTypeAdapter(ResponseWrapper.class, new GoogleResponseDeserializer())
                .addSerializationExclusionStrategy(new JsonExclusionStrategy())
                .addDeserializationExclusionStrategy(new JsonExclusionStrategy())
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Message.TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(Message.TIME_OUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
