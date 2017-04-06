package ee.ut.demo.injector.module;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import dagger.Module;
import dagger.Provides;
import ee.ut.demo.BuildConfig;
import ee.ut.demo.domain.json.JsonExclusionStrategy;
import ee.ut.demo.domain.json.ResponseDeserializer;
import ee.ut.demo.mvp.model.ResponseWrapper;
import ee.ut.demo.domain.repository.Repository;
import ee.ut.demo.domain.repository.RestRepository;
import ee.ut.demo.injector.scope.PerApplication;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

@Module
public class NetworkModule {
    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
    public static final FieldNamingPolicy API_JSON_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

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
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpointUrl)
                .client(client)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit;
    }
}
