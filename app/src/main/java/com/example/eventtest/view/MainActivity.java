package com.example.eventtest.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eventtest.R;
import com.example.eventtest.controller.MainAdapter;
import com.example.eventtest.model.DataAccess;
import com.example.eventtest.model.Event;
import com.example.eventtest.model.SchemaHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter adapter;
    private DataAccess access;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        /* Create or update the database */
        SchemaHelper helper = new SchemaHelper( getApplicationContext() );
        access = new DataAccess( helper );

        Button allEvents = findViewById( R.id.allEvents );
        allEvents.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Event> myEvents = access.getAllLocationOfEvents();
                if (myEvents.size() > 0){
                    AllEventsMap allEventsMap = new AllEventsMap();
                    allEventsMap.setList( myEvents );
                    allEventsMap.show( getSupportFragmentManager(), "map" );
                }else {
                    Toast.makeText( getApplicationContext(), R.string.no_events, Toast.LENGTH_LONG ).show();
                }
            }
        } );

        FloatingActionButton floatingActionButton = findViewById( R.id.floatingActionButton );
        floatingActionButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewEvent();
            }
        } );
        recyclerView = findViewById( R.id.recyclerview );
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        adapter = new MainAdapter( access.getAllEvent() , getApplicationContext(), getSupportFragmentManager(), access);
        recyclerView.setAdapter( adapter );

    }

    /**
     * When the FloatingActionButton is clicked a DialogFragment pops to
     * let user add an event to the Database.
     */
    private void addNewEvent() {
        NewEventFragment fragment = new NewEventFragment();
        fragment.setListener( new NewEventFragment.OnEventFragmentListener() {
            @Override
            public void onEventListener(Event myEvent) {
                long id = access.insert( myEvent );
                if (id != -1){
                    Toast.makeText( getApplicationContext(),
                            R.string.insert_success, Toast.LENGTH_LONG ).show();
                    myEvent.setId( Integer.parseInt( String.valueOf( id ) ) );
                    adapter.updateList(myEvent);
                }else{
                    Toast.makeText( getApplicationContext(),
                            R.string.insert_fail, Toast.LENGTH_LONG ).show();
                }
            }
        } );
        fragment.show(getSupportFragmentManager(), "test");
    }


}
