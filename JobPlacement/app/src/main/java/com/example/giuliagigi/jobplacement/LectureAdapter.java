package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MarcoEsposito90 on 10/11/2015.
 */
public class LectureAdapter implements ListAdapter {

    private ArrayList<Lecture> lectures;
    private boolean isEdit;

    private TextView day, room, schedule;
    private Button modify, delete;



    public LectureAdapter(ArrayList<Lecture> lectures, boolean isEdit){

        Log.println(Log.ASSERT,"LECTUREADAPTER", "size = " + lectures.size());
        this.lectures = lectures;
        this.isEdit = isEdit;
    }



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            Log.println(Log.ASSERT,"LECTUREADAPTER", "inflating");
            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lecture_item_row, parent, false);
        }

        Lecture l = lectures.get(position);

        day = (TextView)convertView.findViewById(R.id.lecture_day_textView);
        room = (TextView)convertView.findViewById(R.id.lecture_room_textView);
        schedule = (TextView)convertView.findViewById(R.id.lecture_schedule_textView);
        modify = (Button)convertView.findViewById(R.id.lecture_modify_button);
        delete = (Button)convertView.findViewById(R.id.lecture_delete_button);

        if(day != null)
            day.setText("" + l.getDayInWeek());

        if(room != null)
            room.setText(l.getRoomName());

        Schedule s = l.getSchedule();
        if(schedule != null)
            schedule.setText("" + s.getStartHour() + ":" + s.getStartMinute() + " - " + s.getEndHour() + ":" + s.getEndMinute());

        if(!isEdit){

            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.lecture_item_container);
            layout.removeView(modify);
            layout.removeView(delete);
        }
        else {

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.println(Log.ASSERT, "LECTUREADAPTER", "asked to modify lecture");
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.println(Log.ASSERT, "LECTUREADAPTER", "asked to delete lecture");
                }
            });
        }

        return convertView;
    }


    @Override
    public int getCount() {
        return lectures.size();
    }


    @Override
    public Object getItem(int position) {
        return lectures.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /* -------------------------------------------------------------------------------------------*/
    /* ----------------------------------- OTHER CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public boolean isEmpty() {
        return lectures.size() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

}
