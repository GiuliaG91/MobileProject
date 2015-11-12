package com.example.giuliagigi.jobplacement;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import java.util.ArrayList;


/**
 * Created by MarcoEsposito90 on 10/11/2015.
 */
public class LectureAdapter implements ListAdapter {

    private ArrayList<Lecture> lectures;
    private boolean isEdit;
    private ArrayList<Boolean> isEditMode;



    public LectureAdapter(ArrayList<Lecture> lectures, boolean isEdit){

        Log.println(Log.ASSERT,"LECTUREADAPTER", "size = " + lectures.size());
        this.lectures = lectures;
        this.isEdit = isEdit;
        isEditMode = new ArrayList<>();
    }



    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lecture_item_row, parent, false);
        }

        Lecture l = lectures.get(position);
        isEditMode.add(position, false);
        final ArrayList<EditText> edits = new ArrayList<>();

        EditText day = (EditText)convertView.findViewById(R.id.lecture_day_editText);
        EditText room = (EditText)convertView.findViewById(R.id.lecture_room_editText);
        EditText startHour = (EditText)convertView.findViewById(R.id.lecture_startHour_editText);
        EditText startMinute = (EditText)convertView.findViewById(R.id.lecture_startMinute_editText);
        EditText endHour = (EditText)convertView.findViewById(R.id.lecture_endHour_editText);
        EditText endMinute = (EditText)convertView.findViewById(R.id.lecture_endMinute_editText);
        final Button modify = (Button)convertView.findViewById(R.id.lecture_modify_button);
        final Button delete = (Button)convertView.findViewById(R.id.lecture_delete_button);

        edits.add(day);
        edits.add(room);
        edits.add(startHour);
        edits.add(startMinute);
        edits.add(endHour);
        edits.add(endMinute);

        if(day != null){
            day.setText(GlobalData.getContext().getResources().getStringArray(R.array.days_complete)[l.getDayInWeek()-1]);
            day.setEnabled(false);
        }

        if(room != null){
            room.setText(l.getRoomName());
            room.setEnabled(false);
        }

        Schedule s = l.getSchedule();
        if(startHour != null && startMinute != null && endHour != null && endMinute != null){
            startHour.setText("" + s.getStartHour());
            startMinute.setText("" + s.getStartMinute());
            endHour.setText("" + s.getEndHour());
            endMinute.setText("" + s.getEndMinute());

            startHour.setEnabled(false);
            startMinute.setEnabled(false);
            endHour.setEnabled(false);
            endMinute.setEnabled(false);
        }

        if(!isEdit){

            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.lecture_item_container);
            modify.setVisibility(View.INVISIBLE);
            modify.setEnabled(false);
            delete.setVisibility(View.INVISIBLE);
            delete.setEnabled(false);
            layout.removeView(modify);
            layout.removeView(delete);
        }
        else {

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean temp = !isEditMode.get(position);
                    isEditMode.add(position, temp);
                    if(!temp) {
                        modify.setBackgroundResource(R.drawable.ic_pencil);
                        Log.println(Log.ASSERT, "LECTUREADAPTER", "isEditMode: " + isEditMode);
                    }
                    else {
                        modify.setBackgroundResource(R.drawable.ic_save);
                        Log.println(Log.ASSERT, "LECTUREADAPTER", "isEditMode: " + isEditMode);
                    }

                    for(EditText et:edits)
                        et.setEnabled(temp);
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
