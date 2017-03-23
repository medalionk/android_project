package ee.ut.demo.data;

import java.util.ArrayList;
import java.util.List;

import ee.ut.demo.mvp.model.Details;
import ee.ut.demo.mvp.model.Event;

/**
 * Created by Bilal Abdullah on 3/17/2017.
 */

public class Data {
    final static String imagePath1 = "http://media.voog.com/0000/0035/5433/photos/16665354_10154334984057671_8397631405729005191_o.jpg";
    public static List<Event> getDummyData() {
        List<Event> events = new ArrayList<>();

        events.add(
                new Event.Builder("Hitman")
                .id(1).location("Tartu").ticket("Free")
                .time("02:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl("http://i.imgur.com/DvpvklR.png")
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(new Event.Builder("Pannkoogihommik")
                .id(2).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Avarongkäik")
                .id(3).location("Tartu").ticket("Free")
                .time("10:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(new Event.Builder("Tudengite öölaulupidu")
                .id(4).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Kastironimise eelvoor")
                .id(5).location("Tartu").ticket("Free")
                .time("03:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Mälukas")
                .id(6).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Pubiralli")
                .id(7).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Tantsumaraton")
                .id(8).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        events.add(
                new Event.Builder("Pannkoogihommik")
                .id(9).location("Tartu").ticket("Free")
                .time("00:00-00:00").url("/students")
                .details(new Details.Builder()
                        .description("Tartu Event")
                        .additionalInfo("Info").imageUrl(imagePath1)
                        .organizer("Organizer").songBook("Songbook").build())
                .build());

        return events;
    }
}
