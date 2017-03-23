package ee.ut.demo.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ee.ut.demo.mvp.model.Details;
import ee.ut.demo.mvp.model.Event;


/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public class EventTable {

    Callable<List<Event>> getEvents(final SQLiteDatabase db, int page) {
        return new Callable<List<Event>>() {
            @Override
            public List<Event> call() {
                String query = "SELECT * FROM " + DatabaseHandler.TABLE_EVENTS;

                Cursor cursor = db.rawQuery(query, null);
                return buildEventList(cursor);
            }
        };
    }

    Callable<List<Event>> getFavouriteEvents(final SQLiteDatabase db) {
        return new Callable<List<Event>>() {
            @Override
            public List<Event> call() {
                String query = "SELECT  * FROM " + DatabaseHandler.TABLE_EVENTS
                        + " WHERE " + DatabaseHandler.KEY_FAVOURITE + " = " + 1;

                Cursor cursor = db.rawQuery(query, null);
                return buildEventList(cursor);
            }
        };
    }

    public Callable<Integer> setFavourite(final SQLiteDatabase db, final int id){
        return new Callable<Integer>() {
            @Override
            public Integer call() {
                ContentValues values = new ContentValues();
                values.put(DatabaseHandler.KEY_FAVOURITE, 1);

                return db.update(DatabaseHandler.TABLE_EVENTS, values, DatabaseHandler.KEY_ID + " = ?",
                        new String[] { String.valueOf(id) });
            }
        };
    }

    public Callable<Integer> unsetFavourite(final SQLiteDatabase db, final int id){

        return new Callable<Integer>() {
            @Override
            public Integer call() {
                ContentValues values = new ContentValues();
                values.put(DatabaseHandler.KEY_FAVOURITE, 0);

                return db.update(DatabaseHandler.TABLE_EVENTS, values, DatabaseHandler.KEY_ID + " = ?",
                        new String[] { String.valueOf(id) });
            }
        };
    }

    public Callable<Integer> addEvents(final SQLiteDatabase db, final List<Event> events) {

        return new Callable<Integer>() {
            @Override
            public Integer call() {
                db.beginTransaction();
                long result = 0L;

                try {
                    ContentValues values = new ContentValues();
                    for (Event event : events) {
                        values.put(DatabaseHandler.KEY_TIME, event.getTime());
                        values.put(DatabaseHandler.KEY_TITLE, event.getTitle());
                        values.put(DatabaseHandler.KEY_LOCATION, event.getLocation());
                        values.put(DatabaseHandler.KEY_TICKET, event.getTicket());
                        values.put(DatabaseHandler.KEY_URL, event.getUrl());
                        values.put(DatabaseHandler.KEY_DESCRIPTION, event.getDetails().getDescription());
                        values.put(DatabaseHandler.KEY_ORGANIZER, event.getDetails().getOrganizer());
                        values.put(DatabaseHandler.KEY_ADDITIONAL_INFO, event.getDetails().getAdditionalInfo());
                        values.put(DatabaseHandler.KEY_SONG_BOOK, event.getDetails().getSongBook());
                        values.put(DatabaseHandler.KEY_IMAGE_URL, event.getDetails().getImageUrl());
                        values.put(DatabaseHandler.KEY_FAVOURITE, 0);

                        result = db.insert(DatabaseHandler.TABLE_EVENTS, null, values);
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                return (int) result;
            }
        };
    }




    private List<Event> buildEventList(Cursor cursor){
        List<Event> events = new ArrayList<>();

        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            do{
                Event event = new Event.Builder(cursor.getString(2))
                        .id(Integer.parseInt(cursor.getString(0)))
                        .time(cursor.getString(1))
                        .location(cursor.getString(3))
                        .ticket(cursor.getString(4))
                        .url(cursor.getString(5))
                        .details(
                                new Details.Builder()
                                        .description(cursor.getString(6))
                                        .organizer(cursor.getString(7))
                                        .additionalInfo(cursor.getString(8))
                                        .songBook(cursor.getString(9))
                                        .imageUrl(cursor.getString(10))
                                        .build())
                        .favorite(Integer.parseInt(cursor.getString(11)))
                        .build();

                events.add(event);
            }while(cursor.moveToNext());
        }

        return events;
    }
}
