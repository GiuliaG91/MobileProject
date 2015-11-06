package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RoomDisplayFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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

            if(floor == 0)  etFloor.setText("" + globalData.getResources().getString(R.string.ground_floor));
            else            etFloor.setText("" + globalData.getResources().getStringArray(R.array.cardinal_numbers)[floor-1] + " " + globalData.getResources().getString(R.string.floor));

            Schedule t = room.getTime();
            if(t!= null){

                etSchedule.setText("" + t.getStartHour() + ":" + t.getStartMinute() + " - " + t.getEndHour() + ":" + t.getEndMinute());
            }
        }


        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return root;
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
