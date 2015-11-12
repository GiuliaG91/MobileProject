package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class LectureDisplayFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "LECTUREDISPLAY";
    private static final String BUNDLE_KEY_COURSE = "lectureDisplay_bundle_course";
    private static int DAY_WIDTH;
    private static final String TAG = "Week Display Activity - LOG: ";

    private RelativeLayout[] lecturesRelativeLayouts = new RelativeLayout[5];

    private GlobalData globalData;
    private ArrayList<Course> courses;
    private boolean isViewPopulated = false;

    public static LectureDisplayFragment newInstance(ArrayList<Course> courses){

        LectureDisplayFragment fragment = new LectureDisplayFragment();
        fragment.courses = courses;
        return fragment;
    }

    public LectureDisplayFragment(){}

    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    // ------------- ON CREATE -----------------------------------------------------------
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        globalData = (GlobalData)activity.getApplicationContext();
    }

    // --------- END ON CREATE -----------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER);

        if(bundle != null){

            ArrayList list = bundle.getList(BUNDLE_KEY_COURSE);
            courses = new ArrayList<Course>();

            for (Object o : list)
                courses.add((Course)o);
        }

        View root = inflater.inflate(R.layout.calendar_week_view, container, false);

        lecturesRelativeLayouts[0] = (RelativeLayout)root.findViewById(R.id.mon_relative_layout);
        lecturesRelativeLayouts[1] = (RelativeLayout)root.findViewById(R.id.tue_relative_layout);
        lecturesRelativeLayouts[2] = (RelativeLayout)root.findViewById(R.id.wed_relative_layout);
        lecturesRelativeLayouts[3] = (RelativeLayout)root.findViewById(R.id.thu_relative_layout);
        lecturesRelativeLayouts[4] = (RelativeLayout)root.findViewById(R.id.fri_relative_layout);

        final ViewTreeObserver vto = lecturesRelativeLayouts[0].getViewTreeObserver();
        if(vto.isAlive()){

            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    DAY_WIDTH = lecturesRelativeLayouts[0].getWidth();
                    if(!isViewPopulated){

                        isViewPopulated = true;
                        populateView();
                    }
                }
            });
        }

        return root;
    }

    // ------------- ON SAVE INSTANCE STATE ----------------------------------------------

    @Override
    public void onSaveInstanceState(Bundle outState) {

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER);
        bundle.putList(BUNDLE_KEY_COURSE,courses);

        super.onSaveInstanceState(outState);

    }


    // --------- END ON SAVE INSTANCE STATE ----------------------------------------------


    // ------------- ON OPTIONS ITEM SELECTED --------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

    private ArrayList<ArrayList<Lecture>> getClusters(ArrayList<Lecture> dayLectures) {

        Collections.sort(dayLectures);
        ArrayList<ArrayList<Lecture>> clusters = new ArrayList<ArrayList<Lecture>>();

        for (int i = 0; i < dayLectures.size(); i++) {

            Lecture l1 = dayLectures.get(i);
            boolean flag = false;

            for (ArrayList<Lecture> c : clusters)
                if (c.contains(l1)) flag = true;

            if(flag) continue;

            ArrayList<Lecture> newCluster = new ArrayList<Lecture>();
            newCluster.add(l1);
            clusters.add(newCluster);

            int last = i;
            flag = true;

            while (flag && last < dayLectures.size()-1){

                if(dayLectures.get(last).isOverlap(dayLectures.get(last+1))){

                    newCluster.add(dayLectures.get(last+1));
                    last++;
                }
                else
                    flag = false;
            }
        }

        return clusters;
    }


    private int getClusterMaxOverlap(ArrayList<Lecture> cluster){

        int width = 1;

        for (int i = 0; i<cluster.size() ; i++){

            int j = i+1;
            boolean overlap = true;
            int currentWidth = 1;

            while (overlap && j<cluster.size()){

                if(cluster.get(i).isOverlap(cluster.get(j))){
                    j++;
                    currentWidth++;
                    if(currentWidth > width) width = currentWidth;
                }
                else
                    overlap = false;
            }
        }

        return width;
    }


    public ArrayList<RelativeLayout> getLectureViews(ArrayList<Lecture> cluster) {

        ArrayList<RelativeLayout> containers = new ArrayList<RelativeLayout>();

        for(int i = 0; i<cluster.size(); i++){

            final Lecture lecture = cluster.get(i);
            View item = setFields(lecture, 0);
            RelativeLayout container = new RelativeLayout(GlobalData.getContext());


            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog lectureDialog = new Dialog(getActivity());

                    View dialogItem = setFields(lecture, 1);
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

            int maxOverlap = getClusterMaxOverlap(cluster);

//            final float density = GlobalData.getContext().getResources().getDisplayMetrics().density;
            float hourHeight = GlobalData.getContext().getResources().getDimension(R.dimen.hour_height);
            int height = (int)(lecture.getSchedule().getMinuteDuration() * hourHeight / 60);
            int startMinute = ((lecture.getSchedule().getStartHour()-8)*60+lecture.getSchedule().getStartMinute());
            int topMargin = (int)(startMinute*hourHeight/60);
            int width = (int)(((float)DAY_WIDTH)/maxOverlap);
            int leftMargin = (int)((i%maxOverlap)*width);

            Log.println(Log.ASSERT, "LECTUREDISPLAY", "hourheight = " + hourHeight + "; startMinute = " + startMinute);
            Log.println(Log.ASSERT, "LECTUREDISPLAY", "dayWidth = " + DAY_WIDTH);
            Log.println(Log.ASSERT,"LECTUREDISPLAY", "height = " + height + "; topMargin = " + topMargin + "; width = " + width + "; leftMargin" + leftMargin);

            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, height);
            param.setMargins(leftMargin, topMargin, 0, 0);
            container.addView(item, param);
            containers.add(container);
        }

        return containers;
    }



    private View setFields(Lecture l, int type){ //metodo utilizzato per riempire i campi delle viste di ogni lezione. Type == 0 --> lecture_ite, Type == 1 --> lecture_dialog_item
        View item;
        if(type == 0) {
            item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item, null);
        }else{
            item = getActivity().getLayoutInflater().inflate(R.layout.lecture_item_dialog, null);
        }

        TextView title = (TextView) item.findViewById(R.id.LECTUREITEM_textView_courseName);
        title.setText(String.valueOf(l.getCourse().getName()));

        TextView dayAndSchedule = (TextView) item.findViewById(R.id.LECTUREITEM_textView_schedule);
        dayAndSchedule.setText(l.getTimeTable());

        TextView room = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Room);
        room.setText(l.getRoomName());

        TextView professor = (TextView) item.findViewById(R.id.LECTUREITEM_textView_Professor);

        try {
            l.getCourse().getProfessor().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        professor.setText(l.getCourse().getProfessor().getName() + " " + l.getCourse().getProfessor().getSurname());

        return item;
    }


    private void populateView(){

        for(int i=0;i<5;i++){


            ArrayList<Lecture> dayLectures = new ArrayList<Lecture>();

            for(Course c: courses)
                for (Lecture l: c.getLectures())
                    if(l.getDayInWeek() == i+1)
                        dayLectures.add(l);

            ArrayList<ArrayList<Lecture>> clusters = getClusters(dayLectures);

            for (ArrayList<Lecture> c : clusters)
                for(RelativeLayout r: getLectureViews(c))
                    lecturesRelativeLayouts[i].addView(r);

        }
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END AUXILIARY METHODS -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////




//----------------------------------------------------------------------------------------------------------------------------------------------




    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- AUXILIARY CLASSES -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

    //------------- LECTURE LIST ADAPTER -------------------------------------------------
//    private class LectureListAdapter extends ArrayAdapter<Lecture> {
//
//
//        private ArrayList<Lecture> lectures;
//
//        public LectureListAdapter(ArrayList<Lecture> lectures) {
//
//            super(getActivity().getApplicationContext(),R.layout.lecture_item,lectures);
//            this.lectures = lectures;
//
//
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){
//
//            Lecture l = lectures.get(position);
//
//            if(convertView == null)
//                convertView = getActivity().getLayoutInflater().inflate(R.layout.lecture_item,parent,false);
//
//            TextView title = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_courseName);
//            title.setText(l.getCourseName());
//
//            TextView dayAndSchedule = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_schedule);
//            dayAndSchedule.setText(l.getTimeTable());
//
//            TextView room = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_Room);
//            room.setText(l.getRoomName());
//
//            TextView professor = (TextView)convertView.findViewById(R.id.LECTUREITEM_textView_Professor);
//            professor.setText(l.getProfessor());
//
//            return convertView;
//        }
//
//
//
//    }
    //--------- END LECTURE LIST ADAPTER -------------------------------------------------


    //------------- HOUR COMPARATOR ------------------------------------------------------
//    class hourComparator implements Comparator<Lecture> {
//        @Override
//        public int compare(Lecture l1, Lecture l2) {
//
//            if(l1.getSchedule().getStartHour() == l2.getSchedule().getStartHour())
//                return l1.getSchedule().getStartMinute() - l2.getSchedule().getStartMinute();
//
//            return l1.getSchedule().getStartHour() - l2.getSchedule().getStartHour();
//        }
//    }
//
//    class durationComparator implements Comparator<Lecture> {
//        @Override
//        public int compare(Lecture l1, Lecture l2) {
//
//            return l1.getSchedule().getMinuteDuration() - l2.getSchedule().getMinuteDuration();
//        }
//    }
    //--------- END HOUR COMPARATOR ------------------------------------------------------

    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END AUXILIARY CLASSES -----------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////
}