package com.example.eventtest.controller;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eventtest.R;
import com.example.eventtest.databinding.RecyclerviewCellBinding;
import com.example.eventtest.model.DataAccess;
import com.example.eventtest.model.Event;
import com.example.eventtest.view.EventDetailFragment;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private ArrayList<Event> events;
    private Context context;
    private FragmentManager manager;
    private DataAccess dataAccess;

    public MainAdapter(ArrayList<Event> myList, Context mContext,
                       FragmentManager fragmentManager, DataAccess access){
        events = myList;
        context = mContext;
        manager = fragmentManager;
        dataAccess = access;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RecyclerviewCellBinding binding;

        public MyViewHolder(@NonNull RecyclerviewCellBinding mBinding) {
            super( mBinding.getRoot() );
            binding = mBinding;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerviewCellBinding binding = DataBindingUtil.inflate( LayoutInflater.from( viewGroup.getContext() ) , R.layout.recyclerview_cell, viewGroup, false );
        return new MyViewHolder( binding );
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.MyViewHolder myViewHolder, final int position) {
        myViewHolder.binding.setEvent( events.get( position ) );
        myViewHolder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEventDetail(myViewHolder.getAdapterPosition());
            }
        } );
    }

    /**
     * When an event is clicked on the RecyclerView, a DialogFragment pop to show user
     * all the informations.
     *
     * @param position the position in the RecyclerView to show the right event to user.
     */
    private void showEventDetail(final int position) {
        EventDetailFragment fragment = new EventDetailFragment();
        fragment.setEvent(dataAccess.getByID( events.get( position ).getId() ));
        fragment.setListener( new EventDetailFragment.OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(boolean response) {
                if (response){
                    Event index = events.get( position );
                    int resp = dataAccess.deleteByID( events.get( position ).getId() );
                    if (resp != 0){
                        deleteFromList( index, position );
                    }else {
                        Toast.makeText( context, "Une erreur est survenue.", Toast.LENGTH_LONG ).show();
                    }
                }
            }
        } );
        fragment.setSupportMapFragment(manager);
        fragment.show( manager, "test" );
    }

    /**
     * Function that update the list of the RecyclerView by doing a query
     * to de Database. After notify the Adapter that the DataSet has changed.
     */
    public void updateList(Event event){
        events.add( event );
        notifyDataSetChanged();
    }

    /**
     * When the user is deleting an event, this function remove it from the ArrayList.
     *
     * @param event the event to remove from the list.
     * @param position the position of the event that is just being remove to notify the adapter
     */
    private void deleteFromList(Event event,int position ){
        events.remove( event );
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
