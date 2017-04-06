package ee.ut.demo.domain.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import ee.ut.demo.mvp.model.Details;
import ee.ut.demo.mvp.model.Element;
import ee.ut.demo.mvp.model.Event;


/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public class EventTable {

    final String DAYS[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    Callable<List<Element>> checkUpdateEvents(final SQLiteDatabase db, final List<Element> elements) {
        return new Callable<List<Element>>() {
            @Override
            public List<Element> call() {

                for (int i = 0; i < elements.size(); i++) {
                    String query = "SELECT  * FROM " + DatabaseHandler.TABLE_EVENTS
                            + " WHERE " + DatabaseHandler.KEY_EVENT_ID + " = " + elements.get(i).getId();

                    Date date = parseDate(elements.get(i).getUpdatedAt());
                    Cursor cursor = db.rawQuery(query, null);
                    if(cursor.moveToFirst() && !parseDate(cursor.getString(6)).after(date)){
                        elements.get(i).setShouldUpdate(false);
                    }else {
                        elements.get(i).setShouldUpdate(true);
                    }
                }

                return elements;
            }
        };
    }

    private Date parseDate(String strDate){
        Date date = new Date();
        strDate = strDate.replace('T', ' ').substring(0, strDate.lastIndexOf('.'));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    Callable<List<Event>> getEvents(final SQLiteDatabase db, final int page) {
        return new Callable<List<Event>>() {
            @Override
            public List<Event> call() {
                String query = "SELECT * FROM " + DatabaseHandler.TABLE_EVENTS
                        + " WHERE " + DatabaseHandler.KEY_DATE + " LIKE "
                        + "'%" + DAYS[page] + "%'";

                return buildEventList(db, query);
            }
        };
    }

    Callable<List<Event>> getFavouriteEvents(final SQLiteDatabase db) {
        return new Callable<List<Event>>() {
            @Override
            public List<Event> call() {
                String query = "SELECT  * FROM " + DatabaseHandler.TABLE_EVENTS
                        + " WHERE " + DatabaseHandler.KEY_FAVOURITE + " = " + 1;

                return buildEventList(db, query);
            }
        };
    }

    public Callable<Integer> toggleFavourite(final SQLiteDatabase db, final String eventID){
        return new Callable<Integer>() {
            @Override
            public Integer call() {
                Integer favorite = 0;
                String query = "SELECT  * FROM " + DatabaseHandler.TABLE_EVENTS
                        + " WHERE " + DatabaseHandler.KEY_EVENT_ID + " = " + eventID;

                Cursor cursor = db.rawQuery(query, null);
                if(cursor.moveToFirst()){
                    favorite = Integer.parseInt(cursor.getString(15));
                    Integer value = 0;
                    if (favorite == 0){
                        value = 1;
                    }

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHandler.KEY_FAVOURITE, value);

                    db.update(DatabaseHandler.TABLE_EVENTS, values, DatabaseHandler.KEY_EVENT_ID + " = ?",
                            new String[] { eventID });
                    return favorite;
                }

                return -1;
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

                        values.put(DatabaseHandler.KEY_START_TIME, event.getStartTime());
                        values.put(DatabaseHandler.KEY_END_TIME, event.getEndTime());
                        values.put(DatabaseHandler.KEY_TITLE, event.getTitle());
                        values.put(DatabaseHandler.KEY_LOCATION, event.getLocation());
                        values.put(DatabaseHandler.KEY_UPDATED_AT,  event.getUpdatedAt());

                        values.put(DatabaseHandler.KEY_DESCRIPTION, event.getDetails().getDescription());
                        values.put(DatabaseHandler.KEY_ORGANIZER, event.getDetails().getOrganizer());
                        values.put(DatabaseHandler.KEY_ADDITIONAL_INFO, event.getDetails().getAdditionalInfo());
                        values.put(DatabaseHandler.KEY_SONG_BOOK, event.getDetails().getSongBook());
                        values.put(DatabaseHandler.KEY_IMAGE_URL, event.getDetails().getImageUrl());
                        values.put(DatabaseHandler.KEY_TICKET,  event.getDetails().getTicket());
                        values.put(DatabaseHandler.KEY_PUBLIC_URL, event.getDetails().getPublicUrl());
                        values.put(DatabaseHandler.KEY_DATE,  event.getDetails().getDate());

                        String query = "SELECT  * FROM " + DatabaseHandler.TABLE_EVENTS
                                + " WHERE " + DatabaseHandler.KEY_EVENT_ID + " = " + event.getId();
                        Cursor cursor = db.rawQuery(query, null);
                        if(cursor.moveToFirst()){
                            db.update(DatabaseHandler.TABLE_EVENTS, values,
                                    DatabaseHandler.KEY_EVENT_ID + " = ?",
                                    new String[] { event.getId() });
                        }else {
                            values.put(DatabaseHandler.KEY_EVENT_ID, event.getId());
                            values.put(DatabaseHandler.KEY_FAVOURITE, 0);
                            result = db.insert(DatabaseHandler.TABLE_EVENTS, null, values);
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                return (int) result;
            }
        };
    }

    private List<Event> buildEventList(final SQLiteDatabase db, String query){
        List<Event> events = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Event event = new Event.Builder(cursor.getString(4))
                        .id(cursor.getString(1))
                        .startTime(cursor.getString(2))
                        .endTime(cursor.getString(3))
                        .location(cursor.getString(5))
                        .updatedAt(cursor.getString(6))
                        .details(
                                new Details.Builder()
                                        .description(cursor.getString(7))
                                        .organizer(cursor.getString(8))
                                        .additionalInfo(cursor.getString(9))
                                        .songBook(cursor.getString(10))
                                        .imageUrl(cursor.getString(11))
                                        .ticket(cursor.getString(12))
                                        .publicUrl(cursor.getString(13))
                                        .date(cursor.getString(14))
                                        .build())
                        .favorite(Integer.parseInt(cursor.getString(15)))
                        .build();

                events.add(event);
            }while(cursor.moveToNext());
        }

        return events;
    }
}
