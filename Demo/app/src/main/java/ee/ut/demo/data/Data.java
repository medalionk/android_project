package ee.ut.demo.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ee.ut.demo.mvp.model.Details;
import ee.ut.demo.mvp.model.Event;

/**
 * Created by Bilal Abdullah on 3/17/2017.
 */

public class Data {
    public static List<Event> getDummyData() {
        List<Event> events = new ArrayList<>();

        events.add(
                new Event.Builder("Hitman")
                .id(1).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(new Event.Builder("Pannkoogihommik")
                .id(2).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Avarongkäik")
                .id(3).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(new Event.Builder("Tudengite öölaulupidu")
                .id(4).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Kastironimise eelvoor")
                .id(5).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Mälukas")
                .id(6).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Pubiralli")
                .id(7).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Tantsumaraton")
                .id(8).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Pannkoogihommik")
                .id(9).location("Tartu").ticket("Free")
                .time("00:00-00:00").path("/students")
                .details(new Details.Builder("Tartu Event")
                        .additionalInfo("Info").imagePath("path")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        return events;
    }
}