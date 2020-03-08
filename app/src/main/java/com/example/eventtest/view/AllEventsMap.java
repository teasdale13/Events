package com.example.eventtest.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventtest.R;
import com.example.eventtest.model.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * DialogFragment that shows all the events on a map.
 */
public class AllEventsMap extends DialogFragment implements OnMapReadyCallback {

    private ArrayList<Event> events;
    SupportMapFragment supportMapFragment;

    public AllEventsMap() {
        // Required empty public constructor
    }

    public static AllEventsMap newInstance() {
        return new AllEventsMap();
    }

    /**
     * Function that set the list of events to show the position
     * on a map.
     *
     * @param list ArrayList of events.
     */
    public void setList(ArrayList<Event> list) {
        events = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_all_events_map, container, false );
        Button ok = view.findViewById( R.id.closeButton );
        ok.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(  );
            }
        } );
        supportMapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById( R.id.allEventsMap );
        supportMapFragment.getMapAsync( this );

        return view;
    }

    public void onButtonPressed() {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (supportMapFragment != null && getFragmentManager() != null) {
            getFragmentManager().beginTransaction().remove( supportMapFragment ).commit();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.dismiss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Event event : events) {
            LatLng latLng = new LatLng( event.getLatitude(), event.getLongitude() );
            MarkerOptions mMarker = new MarkerOptions();
            mMarker.position( latLng ).title( event.getTitle() );
            builder.include( mMarker.getPosition() );
            googleMap.addMarker( mMarker );
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds( bounds, 90 );
        googleMap.animateCamera( cu );
    }

}
