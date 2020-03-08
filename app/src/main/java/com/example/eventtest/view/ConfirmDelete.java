package com.example.eventtest.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventtest.R;

public class ConfirmDelete extends DialogFragment implements View.OnClickListener{

    private OnDeleteInteractionListener mListener;

    public ConfirmDelete() {
        // Required empty public constructor
    }

    public static ConfirmDelete newInstance() {
        return new ConfirmDelete();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
    }

    /**
     * Function that pass a new OnDeleteInteractionListener to delegate the behavior of
     * an event.
     *
     * @param listener The listener that will delegate the behavior.
     */
    public void setListener(OnDeleteInteractionListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_confirm_delete, container, false );
        Button yes = view.findViewById( R.id.yesButton );
        Button no = view.findViewById( R.id.noButton );

        yes.setOnClickListener( this );
        no.setOnClickListener( this );

        return view;
    }

    public void onButtonPressed(boolean response) {
        if (mListener != null) {
            mListener.onDeleteInteraction( response );
            this.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        /* Check witch button has been click */
        switch (v.getId()){
            case R.id.yesButton:
                onButtonPressed( true );
                break;
            case R.id.noButton:
                onButtonPressed( false );
                break;
        }
    }

    public interface OnDeleteInteractionListener {
        void onDeleteInteraction(boolean response);
    }
}
