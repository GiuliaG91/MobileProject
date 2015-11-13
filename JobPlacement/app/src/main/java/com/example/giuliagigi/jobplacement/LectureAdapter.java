package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by MarcoEsposito90 on 10/11/2015.
 */
public class LectureAdapter implements ListAdapter {

    private ArrayList<Lecture> lectures;
    private ArrayList<String>  rooms;
    private boolean isEdit;
    private ArrayList<Boolean> isEditMode;
    private GlobalData globalData;
    private Activity activity;

    public LectureAdapter(ArrayList<Lecture> lectures, boolean isEdit, Activity activity){

        Log.println(Log.ASSERT,"LECTUREADAPTER", "size = " + lectures.size());
        globalData = (GlobalData)activity.getApplicationContext();
        this.activity = activity;
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

        final Lecture l = lectures.get(position);
        isEditMode.add(position, false);
        final ArrayList<EditText> edits = new ArrayList<>();

        final int curDay;
        final String curStartMinute, curStartHour, curEndMinute, curEndHour, curRoom;

        //EditText day = (EditText)convertView.findViewById(R.id.lecture_day_editText);
        final Spinner day = (Spinner)convertView.findViewById(R.id.lecture_day_editText);
        day.setAdapter(new StringAdapter(GlobalData.getContext().getResources().getStringArray(R.array.days_complete)));

        //EditText room = (EditText)convertView.findViewById(R.id.lecture_room_editText);
        final AutoCompleteTextView room = (AutoCompleteTextView)convertView.findViewById(R.id.lecture_room_editText);
        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(globalData.getContext(),R.layout.row_spinner, globalData.getRoomNames());
        room.setAdapter(roomAdapter);

        final EditText startHour = (EditText)convertView.findViewById(R.id.lecture_startHour_editText);
        final EditText startMinute = (EditText)convertView.findViewById(R.id.lecture_startMinute_editText);
        final EditText endHour = (EditText)convertView.findViewById(R.id.lecture_endHour_editText);
        final EditText endMinute = (EditText)convertView.findViewById(R.id.lecture_endMinute_editText);
        final Button modify = (Button)convertView.findViewById(R.id.lecture_modify_button);
        //final Button delete = (Button)convertView.findViewById(R.id.lecture_delete_button);

        //edits.add(day);
        //edits.add(room);
        edits.add(startHour);
        edits.add(startMinute);
        edits.add(endHour);
        edits.add(endMinute);

        curRoom = l.getRoomName();
        curDay = l.getDayInWeek();
        if(day != null){
            day.setSelection(curDay);
            day.setEnabled(false);
        }

        if(room != null){
            room.setText(l.getRoomName());
            room.setEnabled(false);
        }

        Schedule s = l.getSchedule();
        curStartHour ="" + s.getStartHour();
        curStartMinute = "" + s.getStartMinute();
        curEndHour = "" + s.getEndHour();
        curEndMinute = "" + s.getEndMinute();

        if(startHour != null && startMinute != null && endHour != null && endMinute != null){

            startHour.setText(curStartHour);
            startMinute.setText(curStartMinute);
            endHour.setText(curEndHour);
            endMinute.setText(curEndMinute);

            startHour.setEnabled(false);
            startMinute.setEnabled(false);
            endHour.setEnabled(false);
            endMinute.setEnabled(false);
        }

        if(!isEdit){

            LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.lecture_item_container);
            modify.setVisibility(View.INVISIBLE);
            modify.setEnabled(false);
            //delete.setVisibility(View.INVISIBLE);
            //delete.setEnabled(false);
            layout.removeView(modify);
            //layout.removeView(delete);
        }
        else {

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean temp = !isEditMode.get(position);
                    isEditMode.add(position, temp);
                    if(!temp) {
                        modify.setBackgroundResource(R.drawable.ic_pencil);

                        Boolean scheduleChanged = false;
                        if(!(curStartHour.equals(startHour.getText().toString())) || !(curStartMinute.equals(startMinute.getText().toString())) ||
                                !(curEndHour.equals(endHour.getText().toString())) || !(curEndMinute.equals(endMinute.getText().toString())) ||
                                    curDay != (day.getSelectedItemPosition()) || !(curRoom.equals(room.getText().toString())))
                            scheduleChanged = true;

                        Log.println(Log.ASSERT, "LECTUREADAPTER", "scheduleChanged: " + scheduleChanged);

                        if(scheduleChanged){
                            int newStartHour = Integer.parseInt(startHour.getText().toString());
                            int newStartMinute = Integer.parseInt(startMinute.getText().toString());
                            int newEndHour = Integer.parseInt(endHour.getText().toString());
                            int newEndMinute = Integer.parseInt(endMinute.getText().toString());
                            if(newStartHour>=8 && newStartHour<=19 && newEndHour>=8 && newEndHour<=19
                                    && newStartMinute>=0 && newStartMinute<=59 && newEndMinute>=0 && newEndMinute<=59) {
                                l.setDayInWeek(day.getSelectedItemPosition());
                                l.setRoom(room.getText().toString());
                                l.setSchedule(Integer.parseInt(startHour.getText().toString()), Integer.parseInt(startMinute.getText().toString()), Integer.parseInt(endHour.getText().toString()), Integer.parseInt(endMinute.getText().toString()));
                                l.saveEventually();
                                News courseNotice = new News();
                                courseNotice.createNews(7, l.getCourse(),GlobalData.getContext().getResources().getString(R.string.notice_message_course_changes));
                            }
                            else {
                                Toast.makeText(activity, GlobalData.getContext().getResources().getString(R.string.schedule_not_valid), Toast.LENGTH_SHORT).show();
                                startHour.setText(curStartHour);
                                startMinute.setText(curStartMinute);
                                endHour.setText(curEndHour);
                                endMinute.setText(curEndMinute);
                            }
                        }Log.println(Log.ASSERT, "LECTUREADAPTER", "isEditMode: " + isEditMode);
                    }
                    else {
                        modify.setBackgroundResource(R.drawable.ic_save);
                    }

                    for(EditText et:edits)
                        et.setEnabled(temp);
                        day.setEnabled(temp);
                        room.setEnabled(temp);
                }


            });

            /*delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Course c = l.getCourse();

                    Log.println(Log.ASSERT, "LECTUREADAPTER", "asked to delete lecture");
                }
            });*/
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

    protected class StringAdapter extends BaseAdapter {

        public String[] stringArray;

        public StringAdapter(String[] stringArray){
            super();
            this.stringArray = stringArray;
        }

        @Override
        public int getCount() {
            return stringArray.length;
        }

        @Override
        public String getItem(int position) {
            return stringArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = new TextView(GlobalData.getContext());
            TextView tv = (TextView)convertView;
            tv.setText(stringArray[position]);
            //tv.setTextSize(GlobalData.getContext().getResources().getDimension(R.dimen.editText_textSize_small));
            tv.setTextColor(GlobalData.getContext().getResources().getColor(R.color.ColorPrimaryDark));

            return convertView;
        }


    }

}
