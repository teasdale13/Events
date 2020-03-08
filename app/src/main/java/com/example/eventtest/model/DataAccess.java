package com.example.eventtest.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * The Only class that interact with the Database.
 */
public class DataAccess {

    private SQLiteDatabase database;


    public DataAccess(SQLiteOpenHelper helper) {
        database = helper.getWritableDatabase();
    }

    /**
     * Function that returns all the events that are stored in the database. For performance, just the
     * ID, Title and Description are request.
     *
     * @return ArrayList of Events
     */
    public ArrayList<Event> getAllEvent(){
        String query = "SELECT " + EventTable.ID + ", " + EventTable.TITLE + ", " + EventTable.DESCRIPTION + ", " + EventTable.ADDRESS + " FROM " + EventTable.TABLE_NAME;
        ArrayList<Event> events = new ArrayList<>();
        Cursor c = database.rawQuery( query,null );

        while(c.moveToNext()){
            Event event = new Event();
            event.setId( c.getInt( c.getColumnIndexOrThrow( EventTable.ID ) ) );
            event.setTitle( c.getString( c.getColumnIndexOrThrow( EventTable.TITLE ) ) );
            event.setDescription( c.getString( c.getColumnIndexOrThrow( EventTable.DESCRIPTION ) ) );
            event.setAddress( c.getString( c.getColumnIndexOrThrow( EventTable.ADDRESS ) ) );
            events.add( event );
        }
        c.close();
        return events;
    }

    /**
     * Function that get all the localisation of the events to show on a map.
     *
     * @return ArrayList of event.
     */
    public ArrayList<Event> getAllLocationOfEvents(){
        String[] columns = {EventTable.TITLE, EventTable.LONGITUDE, EventTable.LATITUDE};
        ArrayList<Event> events = new ArrayList<>(  );
        Cursor c = database.query( EventTable.TABLE_NAME,columns, null, null,
                null,null, null);

        while (c.moveToNext()){
            Event event = new Event();
            event.setTitle( c.getString( c.getColumnIndexOrThrow( EventTable.TITLE ) ) );
            event.setLatitude( c.getDouble( c.getColumnIndexOrThrow( EventTable.LATITUDE ) ) );
            event.setLongitude( c.getDouble( c.getColumnIndexOrThrow( EventTable.LONGITUDE ) ) );
            events.add( event );
        }
        c.close();
        return events;
    }

    /**
     * Function that insert an event in the Database.
     *
     * @param event A new event to insert into the Database.
     * @return The row ID of the new inserted event.
     */
    public long insert(Event event){
        ContentValues contentValues = new ContentValues(  );
        contentValues.put( EventTable.TITLE, event.getTitle() );
        contentValues.put( EventTable.DESCRIPTION, event.getDescription() );
        contentValues.put( EventTable.LATITUDE, event.getLatitude() );
        contentValues.put( EventTable.LONGITUDE, event.getLongitude() );
        contentValues.put( EventTable.START_DATE, event.getStartDate() );
        contentValues.put( EventTable.END_DATE, event.getEndDate() );
        contentValues.put( EventTable.ADDRESS, event.getAddress() );

        return database.insert( EventTable.TABLE_NAME, null, contentValues );
    }

    /**
     * Function that return an event by his ID.
     *
     * @param ID The ID of the event.
     * @return The event that the user want to consult
     */
    public Event getByID(int ID){
        Event event = new Event(  );
        String selection = EventTable.ID + " = ?";
        Cursor c = database.query( EventTable.TABLE_NAME, null , selection,
                new String[] {String.valueOf( ID )},null,null, null);
        while (c.moveToNext()){
            event.setId( c.getInt( c.getColumnIndexOrThrow( EventTable.ID ) ) );
            event.setTitle( c.getString( c.getColumnIndexOrThrow( EventTable.TITLE ) ) );
            event.setDescription( c.getString( c.getColumnIndexOrThrow( EventTable.DESCRIPTION ) ) );
            event.setLongitude( c.getDouble( c.getColumnIndexOrThrow( EventTable.LONGITUDE ) ) );
            event.setLatitude( c.getDouble( c.getColumnIndexOrThrow( EventTable.LATITUDE ) ) );
            event.setStartDate( c.getLong( c.getColumnIndexOrThrow( EventTable.START_DATE ) ) );
            event.setEndDate( c.getLong( c.getColumnIndexOrThrow( EventTable.END_DATE ) ) );
            event.setAddress( c.getString( c.getColumnIndexOrThrow( EventTable.ADDRESS ) ) );
        }
        c.close();
        return event;
    }

    /**
     * Function that delete an event by his ID.
     *
     * @param ID The ID of the event the user wants to delete.
     * @return The number of rows that are affected by the delete.
     */
    public int deleteByID(int ID){
        String where = EventTable.ID + " = ?";
        return database.delete( EventTable.TABLE_NAME, where, new String[]{String.valueOf( ID )} );
    }
}
