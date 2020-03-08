package com.example.eventtest.model;

import android.annotation.SuppressLint;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Event extends BaseObservable {

    private long startDate, endDate;
    private String Description;
    private String title;
    private String address;
    private double longitude, latitude;
    private int id;

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Bindable
    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Bindable
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Bindable
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Event(){
    }

    /**
     * function that call dateDisplay.
     * @return the event Start Date.
     */
    public String getStartDateToString(){
        return dateDisplay( startDate );
    }

    /**
     * Function that call dateDisplay.
     * @return the event End Date.
     */
    public String getEndDateToString(){
        return dateDisplay( endDate );
    }

    /**
     * Function that takes the date as a long to transfer it to something the user can read (String).
     *
     * @param time The date as a long
     * @return Date as a String.
     */
    @SuppressLint("SimpleDateFormat")
    private String dateDisplay(long time){
        Locale current = Locale.getDefault();
        Date date = new Date(  );
        date.setTime( time );
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, current);
        return dateFormat.format(date);
    }

}
