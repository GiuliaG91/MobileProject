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
        if(s1.getStartHour()==s2.getStartHour() && s1.getStartMinute()==s2.getStartMinute()){
            //se due orari iniziano alla stessa ora si sovrappongono per forza -- TYPE = 1
            s1.setOverlapsType(1);
            s2.setOverlapsType(1);
            return true;
        }
        else if(s1.getEndHour()==s2.getEndHour() && s1.getEndMinute()==s2.getEndMinute()){
            //se due orari finiscono alla stessa ora si sovrappongono per forza -- TYPE = 2
            s1.setOverlapsType(2);
            s2.setOverlapsType(2);
            return true;
        }
        else if(s1.getStartHour()<s2.getStartHour() && s2.getStartHour()<s1.getEndHour()){
            //caso in cui s2 inizia mentre s1 è in corso e finisce dopo s1 -- TYPE = 3
            s1.setOverlapsType(3);
            s2.setOverlapsType(4);
            return true;
        }
        else if(s1.getStartHour() > s2.getStartHour() && s1.getStartHour() < s2.getEndHour() ){
            //caso opposto al precedente -- TYPE = 4
            s1.setOverlapsType(4);
            s2.setOverlapsType(3);
            return true;
        }
        else if(s1.getStartHour()<s2.getStartHour() && s1.getEndHour()>s2.getEndHour()){
            //caso in cui s2 inizia e finisce mentre s1 è in corso -- TYPE = 5
            s1.setOverlapsType(5);
            s2.setOverlapsType(6);
            return true;
        }
        else if(s1.getStartHour() > s2.getStartHour() && s1.getEndHour()<s2.getEndHour()){
            //caso opposto al precedente -- TYPE = 6
            s1.setOverlapsType(6);
            s2.setOverlapsType(5);
            return true;
        }
        else return false;
    }

    public int calculateOverlapseDuration(Schedule s1, Schedule s2, int type){
        int duration = 0;
        if(type == 1 || type == 2){
            duration = Math.max(s1.getMinuteDuration(), s2.getMinuteDuration());
        }
        if(type == 3){
           //somma delle due durate meno la loro sovrapposizione
           duration = s1.getMinuteDuration() + s2.getMinuteDuration() - ((60-s2.getStartMinute()) + s1.getEndMinute() + 60*(s1.getEndHour()-s2.getStartHour()-1));
        }
        if(type == 4){
            //somma delle due durate meno la loro sovrapposizione
           duration = s1.getMinuteDuration() + s2.getMinuteDuration() - ((60-s1.getStartMinute()) + s2.getEndMinute() + 60*(s2.getEndHour()-s1.getStartHour()-1));
        }
        if(type == 5){
            duration = s1.getMinuteDuration();
        }
        if(type == 6){
            duration = s2.getMinuteDuration();
        }
        return duration;
    }

        /*public RelativeLayout setLectureView(Lecture lect) {

        RelativeLayout container = new RelativeLayout(GlobalData.getContext());
        final Lecture lecture = lect;
        final int numOverlapse = lect.getNumOverlapse();
        final float density = GlobalData.getContext().getResources().getDisplayMetrics().density;

        View item = setFields(lecture,0);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog lectureDialog = new Dialog(getActivity());

                View dialogItem = setFields(lecture,1);
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

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(col_width/(numOverlapse+1), (int) (lect.getSchedule().getMinuteDuration() * density));
        param.setMargins(0, (int) (lect.getSchedule().getStartPxl() * density), 0, 0);
        item.setLayoutParams(param);

        container.addView(item);


        return container;
    }*/


    public RelativeLayout setLectureView(Lecture lect) {
        RelativeLayout container = new RelativeLayout(GlobalData.getContext());
        final Lecture lecture = lect;
        final int numOverlapse = lect.getNumOverlapse();
        final float density = GlobalData.getContext().getResources().getDisplayMetrics().density;

        if (numOverlapse == 0) { //caso in cui non ci sono sovrapposizioni di orario
            View item = setFields(lecture,0);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog lectureDialog = new Dialog(getActivity());

                    View dialogItem = setFields(lecture,1);
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
            container.addView(item, param);


        } else { //caso in cui ho almeno una sovrapposizione
            ArrayList<Lecture> tempArray = lect.getLectureOverlapse();
            tempArray.add(lect);
            Collections.sort(tempArray, new durationComparator());

            final int overlapseDuration = calculateOverlapseDuration(tempArray.get(0).getSchedule(), tempArray.get(1).getSchedule(), tempArray.get(0).getSchedule().getOverlapsType());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, (int) (overlapseDuration * density));
            layoutParams.setMargins(0, (int) (tempArray.get(0).getSchedule().getStartPxl() * density), 0, 0);

            //container.setLayoutParams(layoutParams);
            //container.setLayoutParams(param);
            //LinearLayout allViews = new LinearLayout(GlobalData.getContext());

            //GridLayout itemContainer = new GridLayout(GlobalData.getContext());
            LinearLayout itemContainer = new LinearLayout(GlobalData.getContext());
            itemContainer.setWeightSum(numOverlapse+1);

            for(int i = 0; i< tempArray.size(); i++) {
                final Lecture l = tempArray.get(i);
                if (!l.isShown()) {
                    l.setShown(true);

                    //itemContainer.setColumnCount(numOverlapse+1);
                    //itemContainer.setRowCount(1);

                    View item = setFields(l,0);

                    LinearLayout.LayoutParams itemParams = new  LinearLayout.LayoutParams(0,  (int)(l.getSchedule().getMinuteDuration()*density), 1);
                    //itemParams.weight = 1;
                    //itemParams.setMargins(0, (int)(lect.getSchedule().getStartPxl()*density),0,0);
                    //item.setLayoutParams(itemParams);

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog lectureDialog = new Dialog(getActivity());

                            View dialogItem = setFields(l,1);

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


                    //item.setLayoutParams(itemParams);

                    //GridLayout.Spec rowSpan = GridLayout.spec(0, 1);
                    //GridLayout.Spec colspan = GridLayout.spec(i, 1);
                    //GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colspan);
                    itemContainer.addView(item, itemParams);

                }
                container.removeAllViews();
                container.addView(itemContainer, layoutParams); //mantenere qui layoutParams se no non funziona
                //container.removeAllViews();
            }
        }
        return container;
    }



    private View setFields(Lecture l, int type){ //metodo utilizzato per riempire i campi delle viste di ogni lezione. Type == 0 --> lecture_ite, Type == 1 --> lecture_dialog_item
        View item;
        if(type == 0) {
            item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item, null);
        }else{
            item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item_dialog, null);
        }

        TextView title = (TextView) item.findViewById(R.id.LECTUREITEM_textView_courseName);
        title.setText(String.valueOf(l.getCourseName()));

        TextView dayAndSchedule = (TextView) item.findViewById(R.id.LECTUREITEM_textView_schedule);
        dayAndSchedule.setText(l.getTimeTable());

        TextView room = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Room);
        room.setText(l.getRoomName());

        TextView professor = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Professor);
        professor.setText(l.getProfessor());

        return item;
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

    class durationComparator implements Comparator<Lecture> {
        @Override
        public int compare(Lecture l1, Lecture l2) {

            return l1.getSchedule().getMinuteDuration() - l2.getSchedule().getMinuteDuration();
        }
    }
    //--------- END HOUR COMPARATOR ------------------------------------------------------

    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END AUXILIARY CLASSES -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////
}