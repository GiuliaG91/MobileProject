package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;


public class RoomSearch extends Fragment {

    Button display;
    AutoCompleteTextView etRoom;
    FrameLayout roomDisplayContainer;
    private RoomsFileReader roomsFileReader;

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- CONSTRUCTOR -----------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public static RoomSearch newInstance() {
        RoomSearch fragment = new RoomSearch();
        return fragment;
    }

    public RoomSearch() {}


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.println(Log.ASSERT, "ROOMSEARCH", "onAttach");
        GlobalData app = (GlobalData)activity.getApplicationContext();
        roomsFileReader = app.getRoomsFileReader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.println(Log.ASSERT, "ROOMSEARCH", "onCreateView");

        View root = inflater.inflate(R.layout.fragment_room_search, container, false);

        display = (Button)root.findViewById(R.id.displayButton);
        etRoom = (AutoCompleteTextView)root.findViewById(R.id.editTextRoom);
        roomDisplayContainer = (FrameLayout)root.findViewById(R.id.rooms_display_container);

        ArrayAdapter<String> adapterRooms = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, roomsFileReader.getRoomNames());
        etRoom.setAdapter(adapterRooms);

        etRoom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                display.setEnabled(!etRoom.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        display.setEnabled(false);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Room r = roomsFileReader.getRoom(etRoom.getText().toString());

                if(r != null){

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    RoomDisplayFragment rdf = RoomDisplayFragment.newInstance(r);

                    ft.replace(R.id.rooms_display_container,rdf)
                            .commit();

                }
            }
        });

        return root;
    }

    @Override
    public void onDetach() {

        Log.println(Log.ASSERT, "ROOMSEARCH", "onDetach");
        super.onDetach();
    }

}
