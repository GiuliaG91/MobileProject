package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class RoomMapFragment extends Fragment {


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- CONSTRUCTOR -----------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public static RoomMapFragment newInstance(String param1, String param2) {
        RoomMapFragment fragment = new RoomMapFragment();
        return fragment;
    }

    public RoomMapFragment() {
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_room_map, container, false);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }
}
