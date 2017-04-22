package ee.ut.demo.injector.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.BuildConfig;
import ee.ut.demo.domain.json.JsonExclusionStrategy;
import ee.ut.demo.domain.json.ResponseDeserializer;
import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.domain.repository.RestRepository;
import ee.ut.demo.injector.scope.PerApplication;
import ee.ut.demo.mvp.model.ResponseWrapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class NetworkModule {
    private static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
    private static final FieldNamingPolicy API_JSON_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    private static final long TIME_OUT = 60;

    @Provides
    @PerApplication
    Repository provideRepository(Retrofit retrofit) {
        return new RestRepository(retrofit);
    }


    @Provides
    @PerApplication
    Retrofit provideRetrofit() {
        String endpointUrl = BuildConfig.apiEndpointUrl;
        Gson gson = new GsonBuilder()
                .setDateFormat(API_DATE_FORMAT)
                .setFieldNamingPolicy(API_JSON_NAMING_POLICY)
                .registerTypeAdapter(ResponseWrapper.class, new ResponseDeserializer())
                .addSerializationExclusionStrategy(new JsonExclusionStrategy())
                .addDeserializationExclusionStrategy(new JsonExclusionStrategy())
                .create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        okHttpClient.interceptors().add(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
