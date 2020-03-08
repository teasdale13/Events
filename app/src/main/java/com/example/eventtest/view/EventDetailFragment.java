package com.example.eventtest.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventtest.R;
import com.example.eventtest.databinding.FragmentEventDetailBinding;
import com.example.eventtest.model.Event;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDetailFragment extends DialogFragment implements OnMapReadyCallback, View.OnClickListener {


    private OnFragmentInteractionListener mListener;
    private FragmentManager manager;
    SupportMapFragment supportMapFragment;
    private Event event;
    FragmentEventDetailBinding binding;

    public EventDetailFragment() {
        // Required empty public constructor
    }

    public void setSupportMapFragment(FragmentManager mManager) {
        manager = mManager;
    }

    public void setListener(OnFragmentInteractionListener listener){
        mListener = listener;
    }

    public static EventDetailFragment newInstance() {
        return new EventDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event_detail, container, false );
        binding.setEvent( event );
        supportMapFragment = (SupportMapFragment) manager
                .findFragmentById( R.id.mapFragment );
        supportMapFragment.getMapAsync( this );

        binding.okButton.setOnClickListener( this );
        binding.deleteButton.setOnClickListener( this );
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton:
                onButtonPressed( false );
                break;
            case R.id.deleteButton:
                confirmDelete();
                break;
        }
    }

    /**
     * This function is called when the Delete button is clicked. A DialogFragment pops to
     * ask the user if he really want to delete this event.
     */
    private void confirmDelete() {
        ConfirmDelete fragment = new ConfirmDelete();
        fragment.setListener( new ConfirmDelete.OnDeleteInteractionListener() {
            @Override
            public void onDeleteInteraction(boolean response) {
                if (response){
                    onButtonPressed( true );
                }else {
                    onButtonPressed( false );
                }
            }
        } );
        fragment.show( manager, "delete" );
    }

    public void onButtonPressed(boolean response) {
        if (mListener != null) {
            mListener.onFragmentInteraction( response );
            this.dismiss();
        }
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
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng( binding.getEvent().getLatitude(),binding.getEvent().getLongitude() );
        MarkerOptions mMarker = new MarkerOptions();
        mMarker.position( latLng ).title( binding.getEvent().getTitle());
        googleMap.addMarker( mMarker );
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom( latLng, 16 );
        googleMap.animateCamera( cu );
    }

    public void setEvent(Event myEvent) {
        event = myEvent;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean response);
    }

}
