package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.Dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class LectureDisplayFragment extends Fragment {

    private static final String TAG = "Week Display Activity - LOG: ";
    private RelativeLayout[] lecturesRelativeLayouts = new RelativeLayout[5];
    private LecturesFileReader lecturesFileReader;
    private String professor, course;

    public static LectureDisplayFragment newInstance(String course, String professor){

        LectureDisplayFragment fragment = new LectureDisplayFragment();
        fragment.setCourse(course);
        fragment.setProfessor(professor);
        return fragment;
    }

    public LectureDisplayFragment(){}

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    // ------------- ON CREATE -----------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        Log.println(Log.ASSERT,"LECTUREFRAGMENT", "lecture Fragment attach");
//        setContentView(R.layout.calendar_week_view);

        GlobalData app = (GlobalData)activity.getApplicationContext();
        lecturesFileReader = app.getLecturesFileReader();


    }

    // --------- END ON CREATE -----------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.calendar_week_view, container, false);

        lecturesRelativeLayouts[0] = (RelativeLayout)root.findViewById(R.id.mon_relative_layout);
        lecturesRelativeLayouts[1] = (RelativeLayout)root.findViewById(R.id.tue_relative_layout);
        lecturesRelativeLayouts[2] = (RelativeLayout)root.findViewById(R.id.wed_relative_layout);
        lecturesRelativeLayouts[3] = (RelativeLayout)root.findViewById(R.id.thu_relative_layout);
        lecturesRelativeLayouts[4] = (RelativeLayout)root.findViewById(R.id.fri_relative_layout);


//        Bundle extras = getIntent().getExtras();
//        String course = "Programmazione di sistema",professor = null;
//
//        if(extras.getBoolean(GlobalData.BUNDLE_KEY_CONTAINS_COURSE_NAME))
//            requestedCourse = extras.getString(GlobalData.BUNDLE_KEY_COURSE_NAME);
//        else
//            requestedCourse = null;
//
//        if(extras.getBoolean(GlobalData.BUNDLE_KEY_CONTAINS_PROFESSOR_NAME))
//            requestedProfessor = extras.getString(GlobalData.BUNDLE_KEY_PROFESSOR_NAME);
//        else
//            requestedProfessor = null;


        for(int i=0;i<5;i++){

            ArrayList<Lecture> lectures = lecturesFileReader.getDayLectures(course,professor,i+1);
            Collections.sort(lectures, new hourComparator());

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
                RelativeLayout item = setLectureView(showedLecture);
                lecturesRelativeLayouts[i].addView(item);

            }

        }

        return root;
    }


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
        if(((s2.getStartHour() < s1.getStartHour() || (s2.getStartHour() == s1.getStartHour() && s2.getStartMinute()<=s1.getStartMinute()))
                && s1.getEndHour() < s2.getEndHour() ||  (s1.getEndHour() == s2.getEndHour() && s1.getEndMinute() <= s2.getEndMinute())) ||
                (s1.getStartHour() < s2.getStartHour() || (s1.getStartHour() == s2.getStartHour() && s1.getStartMinute()<=s2.getStartMinute()))
                        && s2.getEndHour() < s1.getEndHour() ||  (s2.getEndHour() == s1.getEndHour() && s2.getEndMinute() <= s1.getEndMinute())){

            return true;
        }
        else return false;
    }

    public RelativeLayout setLectureView(Lecture lect) {
        RelativeLayout container = new RelativeLayout(GlobalData.getContext());
        final String name = lect.getCourseName();
        final String professorName = lect.getProfessor();
        final String timeTable = lect.getTimeTable();
        final String roomName = lect.getRoomName();
        final int numOverlapse = lect.getNumOverlapse();
        final float density = GlobalData.getContext().getResources().getDisplayMetrics().density;

        //container.setOrientation(LinearLayout.HORIZONTAL);

        if (numOverlapse == 0) {
            View item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item, null);

            TextView title = (TextView) item.findViewById(R.id.LECTUREITEM_textView_courseName);
            title.setText(String.valueOf(numOverlapse));

            TextView dayAndSchedule = (TextView) item.findViewById(R.id.LECTUREITEM_textView_schedule);
            dayAndSchedule.setText(timeTable);

            TextView room = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Room);
            room.setText(roomName);

            TextView professor = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Professor);
            professor.setText(professorName);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog lectureDialog = new Dialog(getActivity());
                    View dialogItem = getActivity().getLayoutInflater().inflate(R.layout.lecture_item_dialog, null);

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

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (lect.getSchedule().getMinuteDuration() * density));
            param.setMargins(0, (int) (lect.getSchedule().getStartPxl() * density), 0, 0);
            container.setLayoutParams(param);
            container.addView(item);

        } else {
            ArrayList<Lecture> tempArray = lect.getLectureOverlapse();
            tempArray.add(lect);
            Collections.sort(tempArray, new hourComparator());

            final int duration = (60 - tempArray.get(0).getSchedule().getStartMinute()) + tempArray.get(numOverlapse - 1).getSchedule().getEndMinute() + 60 * (tempArray.get(numOverlapse - 1).getSchedule().getEndHour() - tempArray.get(0).getSchedule().getStartHour() - 1);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (duration * density));
            layoutParams.setMargins(0, (int) (tempArray.get(0).getSchedule().getStartPxl() * density), 0, 0);
            container.setLayoutParams(layoutParams);

            //container.setLayoutParams(param);
            LinearLayout allViews = new LinearLayout(GlobalData.getContext());

            for(int i = 0; i< tempArray.size(); i++) {
                final Lecture l = tempArray.get(i);
                if (!l.isShown()) {
                    l.setShown(true);

                    LinearLayout itemLayout = new LinearLayout(GlobalData.getContext());
                    View item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item, null);

                    TextView title = (TextView) item.findViewById(R.id.LECTUREITEM_textView_courseName);
                    title.setText(String.valueOf(numOverlapse));

                    TextView dayAndSchedule = (TextView) item.findViewById(R.id.LECTUREITEM_textView_schedule);
                    dayAndSchedule.setText(String.valueOf(duration));

                    TextView room = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Room);
                    room.setText(l.getRoomName());

                    TextView professor = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Professor);
                    professor.setText(l.getProfessor());

                    LinearLayout.LayoutParams itemParams = new  LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  (int)(lect.getSchedule().getMinuteDuration()*density), .5f);
                    itemParams.setMargins(0, (int)(lect.getSchedule().getStartPxl()*density),0,0);
                    item.setLayoutParams(itemParams);

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog lectureDialog = new Dialog(getActivity());
                            View dialogItem = getActivity().getLayoutInflater().inflate(R.layout.lecture_item_dialog, null);

                            TextView title = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_courseName);
                            title.setText(l.getCourseName());

                            TextView dayAndSchedule = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_schedule);
                            dayAndSchedule.setText(l.getTimeTable());

                            TextView room = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_Room);
                            room.setText(l.getRoomName());

                            TextView professor = (TextView) dialogItem.findViewById(R.id.LECTUREITEM_textView_Professor);
                            professor.setText(l.getProfessor());

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
                    //singleView.addView(item);

                    allViews.addView(item,i);

                }
                container.removeAllViews();
                container.addView(allViews);
            }
        }
        return container;
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

            super(getActivity().getApplicationContext(),R.layout.lecture_item,lectures);
            this.lectures = lectures;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            Lecture l = lectures.get(position);

            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.lecture_item,parent,false);

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