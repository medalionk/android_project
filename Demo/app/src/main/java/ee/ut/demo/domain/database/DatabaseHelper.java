package ee.ut.demo.domain.database;

import android.content.Context;

/**
 * Created by Bilal Abdullah on 3/22/2017.
 */

public class DatabaseHelper {
    private DatabaseHelper(){

    }

    private static DatabaseHandler databaseHandler;


    public static synchronized DatabaseHandler getDataBaseHandler(Context context){
        if(databaseHandler == null){
            databaseHandler = new DatabaseHandler(context.getApplicationContext(), new EventTable());
        }

        return databaseHandler;
    }

    public static synchronized void closeDataBaseHandler(Context context){
        if(databaseHandler != null){
            databaseHandler.close();
        }

        databaseHandler = null;
    }
}
