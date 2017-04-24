package ee.ut.demo.helpers;

import com.google.gson.FieldNamingPolicy;

/**
 * Created by Bilal Abdullah on 4/22/2017.
 */

public class Message {
    public final static String ERR_MSG_DB = "Error connecting to local database!!!";
    public final static String ERR_MSG_INTERNET = "Error connecting to the internet!!!";
    public final static String ERR_MSG_NO_INTERNET = "Not connected to the internet!!!";

    public static final String API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
    public static final FieldNamingPolicy API_JSON_NAMING_POLICY = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
    public static final long TIME_OUT = 60;
}
