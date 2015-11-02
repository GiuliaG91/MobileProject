package com.example.giuliagigi.jobplacement;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class WeekDisplayActivity extends ActionBarActivity {

    private static final String TAG = "Week Display Activity - LOG: ";
    private RelativeLayout[] lecturesRelativeLayouts = new RelativeLayout[5];
    private LecturesFileReader lecturesFileReader;

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    // ------------- ON CREATE -----------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_week_view);

        GlobalData app = (GlobalData)getApplicationContext();
        lecturesFileReader = app.getLecturesFileReader();

        lecturesRelativeLayouts[0] = (RelativeLayout)findViewById(R.id.mon_relative_layout);
        lecturesRelativeLayouts[1] = (RelativeLayout)findViewById(R.id.tue_relative_layout);
        lecturesRelativeLayouts[2] = (RelativeLayout)findViewById(R.id.wed_relative_layout);
        lecturesRelativeLayouts[3] = (RelativeLayout)findViewById(R.id.thu_relative_layout);
        lecturesRelativeLayouts[4] = (RelativeLayout)findViewById(R.id.fri_relative_layout);


        Bundle extras = getIntent().getExtras();
        String requestedCourse,requestedProfessor;

        if(extras.getBoolean(GlobalData.BUNDLE_KEY_CONTAINS_COURSE_NAME))
            requestedCourse = extras.getString(GlobalData.BUNDLE_KEY_COURSE_NAME);
        else
            requestedCourse = null;

        if(extras.getBoolean(GlobalData.BUNDLE_KEY_CONTAINS_PROFESSOR_NAME))
            requestedProfessor = extras.getString(GlobalData.BUNDLE_KEY_PROFESSOR_NAME);
        else
            requestedProfessor = null;


        for(int i=0;i<5;i++){

            ArrayList<Lecture> lectures = lecturesFileReader.getDayLectures(requestedCourse,requestedProfessor,i+1);
            Collections.sort(lectures, new hourComparator());

            /*for (int j = 0; j < lectures.size(); j++){
                final Lecture cur = lectures.get(j);
                for (int z = 0; z < lectures.size(); z++){
                    if(z!=j){
                        final Lecture compare = lectures.get(z);
                        if(compareSchedule(cur.getSchedule(), compare.getSchedule()) && !compare.containsOverlapse(cur)){
                            compare.setNumOverlapse(compare.getNumOverlapse()+1);
                            cur.setNumOverlapse(cur.getNumOverlapse()+1);
                            compare.addLectureOverlapse(cur);
                            cur.addLectureOverlapse(compare);
                        }
                    }
                }

            }*/


            for (int it = 0; it < lectures.size(); it++){
                Lecture showedLecture = lectures.get(it);
                for (int z = 0; z < lectures.size(); z++){
                    if(z!=it){
                        final Lecture compare = lectures.get(z);
                        if(compareSchedule(showedLecture.getSchedule(), compare.getSchedule()) && !compare.containsOverlapse(showedLecture)){
                            compare.setNumOverlapse(compare.getNumOverlapse()+1);
                            showedLecture.setNumOverlapse(showedLecture.getNumOverlapse()+1);
                            compare.addLectureOverlapse(showedLecture);
                            showedLecture.addLectureOverlapse(compare);
                        }
                    }
                }
                View item = setLectureView(showedLecture);

                float density = GlobalData.getContext().getResources().getDisplayMetrics().density;

                RelativeLayout.LayoutParams param = new  RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  (int)(showedLecture.getSchedule().getMinuteDuration()*density));
                param.setMargins(0, (int)(showedLecture.getSchedule().getStartPxl()*density),0,0);
                item.setLayoutParams(param);

                lecturesRelativeLayouts[i].addView(item);

            }

        }
    }



    // --------- END ON CREATE -----------------------------------------------------------



    // ------------- ON CREATE MENU OPTIONS ----------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week_display, menu);
        return true;
    }
    // --------- END ON CREATE MENU OPTIONS ----------------------------------------------



    // ------------- ON OPTIONS ITEM SELECTED --------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // --------- END ON OPTIONS ITEM SELECTED --------------------------------------------


    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////




//----------------------------------------------------------------------------------------------------------------------------------------------




    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- AUXILIARY METHODS -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    public boolean compareSchedule(Schedule s1, Schedule s2){
        if(((s1.getStartHour() < s2.getStartHour() || (s1.getStartHour() == s2.getStartHour() && s1.getStartMinute()<=s2.getStartMinute()))
                && s1.getEndHour() < s2.getEndHour() ||  (s1.getEndHour() == s2.getEndHour() && s1.getEndMinute() <= s2.getEndMinute())) ||
                (s2.getStartHour() < s1.getStartHour() || (s2.getStartHour() == s1.getStartHour() && s2.getStartMinute()<=s1.getStartMinute()))
                        && s2.getEndHour() < s1.getEndHour() ||  (s2.getEndHour() == s1.getEndHour() && s2.getEndMinute() <= s1.getEndMinute())){



            return true;
        }
        else return false;
    }

    public View setLectureView(Lecture lect){
        GridLayout grid = new GridLayout(GlobalData.getContext());
        final String name = lect.getCourseName();
        final String professorName = lect.getProfessor();
        final String timeTable = lect.getTimeTable();
        final String roomName = lect.getRoomName();

        if(lect.getNumOverlapse() == 0) {
            grid.setColumnCount(1);
            grid.setRowCount(1);
            View item = getLayoutInflater().inflate(R.layout.lecture_item, null);

            TextView title = (TextView) item.findViewById(R.id.LECTUREITEM_textView_courseName);
            title.setText(name);

            TextView dayAndSchedule = (TextView) item.findViewById(R.id.LECTUREITEM_textView_schedule);
            dayAndSchedule.setText(timeTable);

            TextView room = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Room);
            room.setText(roomName);

            TextView professor = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Professor);
            professor.setText(professorName);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog lectureDialog = new Dialog(WeekDisplayActivity.this);
                    View dialogItem = getLayoutInflater().inflate(R.layout.lecture_item_dialog, null);

                    TextView title = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_courseName);
                    title.setText(name);

                    TextView dayAndSchedule = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_schedule);
                    dayAndSchedule.setText(timeTable);

                    TextView room = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_Room);
                    room.setText(roomName);

                    TextView professor = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_Professor);
                    professor.setText(professorName);

                    lectureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    lectureDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    lectureDialog.setContentView(dialogItem);
                    Button closingButton = (Button) dialogItem.findViewById(R.id.lecture_item_ok_button);
                    closingButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lectureDialog.dismiss();
                        }
                    });

                    lectureDialog.show();
                }
            });
            grid.addView(item);
        }
        else {
            int numOverlapse = lect.getNumOverlapse();
            grid.setColumnCount(numOverlapse);
            grid.setRowCount(1);

        }

        return grid;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END AUXILIARY METHODS -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////




//----------------------------------------------------------------------------------------------------------------------------------------------




    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- AUXILIARY CLASSES -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    //------------- LECTURE LIST ADAPTER -------------------------------------------------
    private class LectureListAdapter extends ArrayAdapter<Lecture> {


        private ArrayList<Lecture> lectures;

        public LectureListAdapter(ArrayList<Lecture> lectures) {

            super(getApplicationContext(),R.layout.lecture_item,lectures);
            this.lectures = lectures;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            Lecture l = lectures.get(position);

            if(convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.lecture_item,parent,false);

            TextView title = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_courseName);
            title.setText(l.getCourseName());

            TextView dayAndSchedule = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_schedule);
            dayAndSchedule.setText(l.getTimeTable());

            TextView room = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_Room);
            room.setText(l.getRoomName());

            TextView professor = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_Professor);
            professor.setText(l.getProfessor());

            return convertView;
        }



    }
    //--------- END LECTURE LIST ADAPTER -------------------------------------------------


    //------------- HOUR COMPARATOR ------------------------------------------------------
    class hourComparator implements Comparator<Lecture> {
        @Override
        public int compare(Lecture l1, Lecture l2) {

            if(l1.getSchedule().getStartHour() == l2.getSchedule().getStartHour())
                return l1.getSchedule().getStartMinute() - l2.getSchedule().getStartMinute();

            return l1.getSchedule().getStartHour() - l2.getSchedule().getStartHour();
        }
    }
    //--------- END HOUR COMPARATOR ------------------------------------------------------

    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END AUXILIARY CLASSES -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////
}