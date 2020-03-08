package com.example.eventtest.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventtest.R;
import com.example.eventtest.model.Event;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewEventFragment extends DialogFragment implements View.OnClickListener{

    private OnEventFragmentListener mListener;
    Calendar myCalendar;
    EditText startDate;
    EditText endDate;
    String completeAddress;
    EditText address, title, description;
    Button confirmButton;

    public NewEventFragment() {
        // Required empty public constructor
    }

    public static NewEventFragment newInstance() {
        return new NewEventFragment();
    }

    public void setListener(OnEventFragmentListener listener){
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View myView = inflater.inflate( R.layout.fragment_new_event, container, false );

        myCalendar = Calendar.getInstance();
        startDate = myView.findViewById( R.id.StartDate );
        address = myView.findViewById( R.id.editAddress );
        title = myView.findViewById( R.id.editTitle );
        description = myView.findViewById( R.id.editDescription );
        startDate.setOnClickListener( this );
        endDate = myView.findViewById( R.id.EndDate );
        endDate.setOnClickListener( this );

        confirmButton = myView.findViewById( R.id.confirmButton );
        confirmButton.setOnClickListener( this );

        return myView;
    }

    /**
     * Update the text (Date) on the EditText with a specific Date format
     *
     * @param myCalendar The calendar
     * @param id The ID of the EditText that has been clicked
     */
    private void updateText(Calendar myCalendar, int id) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        switch (id){
            case R.id.StartDate:
                startDate.setText( sdf.format(myCalendar.getTime()) );
                break;
            case R.id.EndDate:
                endDate.setText( sdf.format(myCalendar.getTime()) );
                break;
        }
    }

    public void onButtonPressed(Event event) {
        if (mListener != null) {
            mListener.onEventListener( event );
            this.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.confirmButton){
            if (allIsFill()){
                buildEvent();
            }else {
                showWhatHaveToBeCompleted();
            }
        }else {
            new DatePickerDialog( getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    myCalendar.set( Calendar.YEAR, year );
                    myCalendar.set( Calendar.MONTH, month );
                    myCalendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
                    updateText( myCalendar, v.getId() );
                }
            }, myCalendar
                    .get( Calendar.YEAR ), myCalendar.get( Calendar.MONTH ),
                    myCalendar.get( Calendar.DAY_OF_MONTH ) ).show();
        }
    }

    /**
     * Function that check if the required fields are filled.
     *
     * @return return true if all the fields are completed and false if some fields are
     *      * missing
     */
    public boolean allIsFill(){
        return (!startDate.getText().toString().equals( "" )) &&
                (!endDate.getText().toString().equals( "" ) && (!title.getText().toString().equals( "" )) &&
                        (!description.getText().toString().equals( "" ))&& (!address.getText().toString().equals( "" )));
    }

    /**
     * If some informations is missing to create an event, the user will
     * be notified by a red line under the missing field and a text as an hint.
     */
    private void showWhatHaveToBeCompleted() {
        EditText[] editTexts = {startDate, endDate, address , description, title };
        for (EditText edit: editTexts) {
            if (edit.getText().toString().trim().equals( "" )){
                edit.setHint( R.string.field_needed );
                edit.getBackground().setTint( Color.RED );
            }
        }
    }

    /**
     * Function that build an event to store it into the database.
     */
    private void buildEvent() {
        Event myEvent = new Event(  );
        LatLng latLng = getLocationFromAddress( address.getText().toString() );
        if (latLng != null){
            myEvent.setLongitude( latLng.longitude  );
            myEvent.setLatitude( latLng.latitude );
            myEvent.setTitle( title.getText().toString() );
            myEvent.setDescription( description.getText().toString() );
            myEvent.setStartDate( dateToLong(startDate.getText().toString()) );
            myEvent.setEndDate( dateToLong( endDate.getText().toString() ) );
            myEvent.setAddress( completeAddress );
            onButtonPressed( myEvent );
        }else {
            Toast.makeText( getContext(), R.string.no_address_found, Toast.LENGTH_LONG ).show();
        }
    }

    /**
     * Function that take a String and return a long to store in the database.
     *
     * @param myDate String date to transfer into long
     *
     * @return the value of the date (long)
     */
    @SuppressLint("SimpleDateFormat")
    private long dateToLong(String myDate) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
        try {
            Date date = dateFormat.parse( myDate );
            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Function that takes the address on the event and return the position of it.
     *
     * @param strAddress Address of the event as a String.
     * @return The position of the event as a LatLng (latitude, longitude).
     */
    private LatLng getLocationFromAddress(String strAddress){
        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        LatLng latLng = null;
        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address.size() == 0) {
                return null;
            }else {
                Address location = address.get(0);
                if (location != null){
                    completeAddress = location.getAddressLine( 0 );
                    location.getLatitude();
                    location.getLongitude();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    public interface OnEventFragmentListener {
        void onEventListener(Event event);
    }
}
