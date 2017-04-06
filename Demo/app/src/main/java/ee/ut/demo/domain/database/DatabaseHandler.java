package ee.ut.demo.domain.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Singleton;

import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */
@Singleton
public class DatabaseHandler extends SQLiteOpenHelper implements Database{

    private static final String DATABASE_NAME = "psearchdb.db";
    private static final int DATABASE_VERSION = 8;
    private static final String TAG = "SQLITE_HELPER";

    public static final String TABLE_EVENTS = "events";
    public static final String KEY_ID = "_id";

    public static final String KEY_EVENT_ID = "event_id";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_UPDATED_AT = "updated_at";

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_ADDITIONAL_INFO = "additional_info";
    public static final String KEY_SONG_BOOK = "song_book";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_TICKET = "ticket";
    public static final String KEY_PUBLIC_URL = "public_url";
    public static final String KEY_DATE = "date";

    public static final String KEY_FAVOURITE = "favorite";

    private static final String DATABASE_CREATE_EVENTS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EVENTS + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EVENT_ID + " TEXT NOT NULL, " + KEY_START_TIME + " TEXT NOT NULL, "
            + KEY_END_TIME + " TEXT, " + KEY_TITLE + " TEXT NOT NULL, "
            + KEY_LOCATION + " TEXT NOT NULL, " + KEY_UPDATED_AT + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT, " + KEY_ORGANIZER + " TEXT, "
            + KEY_ADDITIONAL_INFO + " TEXT, " + KEY_SONG_BOOK + " TEXT, "
            + KEY_IMAGE_URL + " TEXT, " + KEY_TICKET + " TEXT NOT NULL, "
            + KEY_PUBLIC_URL + " TEXT NOT NULL, " + KEY_DATE + " TEXT NOT NULL, "
            + KEY_FAVOURITE + " INTEGER )";

    private EventTable mEventTable;

    public DatabaseHandler(Context context, EventTable eventTable) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mEventTable = eventTable;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        this.onCreate(db);
    }

    @Override
    public Observable<List<Event>> getEventsByPage(int page){
        return makeObservable(mEventTable.getEvents(getReadableDatabase(), page))
                .subscribeOn(Schedulers.computation());

    }

    @Override
    public Observable<List<Event>> getFavouriteEvents(){
        return makeObservable(mEventTable.getFavouriteEvents(getReadableDatabase()))
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Integer> toggleFavourite(String id){
        return makeObservable(mEventTable.toggleFavourite(getReadableDatabase(), id))
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Integer> addEvents(List<Event> events){
        return makeObservable(mEventTable.addEvents(getReadableDatabase(), events))
                .subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<List<Element>> checkUpdateEvents(List<Element> elements) {
        return makeObservable(mEventTable.checkUpdateEvents(getReadableDatabase(), elements))
                .subscribeOn(Schedulers.computation());
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> sub) {
                        try {
                            sub.onNext(func.call());
                            sub.onCompleted();
                        } catch(Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }
}
