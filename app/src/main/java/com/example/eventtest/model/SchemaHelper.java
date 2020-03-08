package com.example.eventtest.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Class that create/update the Database
 */
public class SchemaHelper extends SQLiteOpenHelper {

    public static String DBNAME = "event.db";
    private static int VERSION = 2;

    public SchemaHelper(@Nullable Context context){
        super( context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE event(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , title TEXT NOT NULL, " +
                "description TEXT NOT NULL, longitude DOUBLE , latitude DOUBLE, startDate TEXT NOT NULL, " +
                "endDate TEXT NOT NULL, address TEXT);" );
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "ALTER TABLE " + EventTable.TABLE_NAME + " ADD COLUMN " + EventTable.ADDRESS + " TEXT" );
    }
}
