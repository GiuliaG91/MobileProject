package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RoomDisplayFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "ROOMDISPLAY";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE__KEY_ROOM = "bundle_room";

    private Room room;
    private TextView etName, etFloor, etBuilding, etSchedule;
    private Button showMapButton;
    GlobalData globalData;

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- CONSTRUCTOR -----------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    public static RoomDisplayFragment newInstance(Room room) {
        RoomDisplayFragment fragment = new RoomDisplayFragment();
        fragment.room = room;
        return fragment;
    }

    public RoomDisplayFragment() {}


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        globalData = (GlobalData)activity.getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null)
                room = (Room)bundle.get(BUNDLE__KEY_ROOM);
        }

        View root = inflater.inflate(R.layout.fragment_room_display, container, false);

        etName = (TextView)root.findViewById(R.id.room_name_textView);
        etFloor = (TextView)root.findViewById(R.id.room_floor_textView);
        etBuilding = (TextView)root.findViewById(R.id.room_building_textView);
        etSchedule = (TextView)root.findViewById(R.id.room_schedule_textView);
        showMapButton = (Button)root.findViewById(R.id.room_show_map_button);

        if(room != null){

            etName.setText(room.getName());
            etBuilding.setText(room.getBuilding());

            int floor  = room.getFloor();

            if(floor < 0 )      etFloor.setText("" + globalData.getResources().getString(R.string.basement_floor));
            else if(floor == 0) etFloor.setText("" + globalData.getResources().getString(R.string.ground_floor));
            else                etFloor.setText("" + globalData.getResources().getStringArray(R.array.cardinal_numbers)[floor-1] + " " + globalData.getResources().getString(R.string.floor));

            Schedule t = room.getTime();
            if(t!= null){

                etSchedule.setText("" + t.getStartHour() + ":" + t.getStartMinute() + " - " + t.getEndHour() + ":" + t.getEndMinute());
            }
        }


        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                RoomMapFragment fragment = RoomMapFragment.newInstance(room);

                fragmentManager.beginTransaction()
                        .replace(R.id.tab_Home_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if(room != null){

            String tail = room.toString();
            outState.putString(BUNDLE_KEY_TAIL,tail);

            MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
            bundle.put(BUNDLE__KEY_ROOM,room);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
