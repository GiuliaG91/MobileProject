package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class CourseLecturesFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "COURSEDETAIL";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_COURSE = "bundle_course";


    private View root;
    private ListView lecturesListView;
    private LectureAdapter lectureAdapter;
    private Activity activity;
    private GlobalData globalData;
    private Course course;
    private boolean isEdit;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseLecturesFragment newInstance(Course course, boolean isEdit) {

        CourseLecturesFragment fragment = new CourseLecturesFragment();
        fragment.course = course;
        fragment.course.cacheData();
        fragment.isEdit = isEdit;

        return fragment;
    }

    public CourseLecturesFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        globalData = (GlobalData)activity.getApplicationContext();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null)
                course = (Course)bundle.get(BUNDLE_KEY_COURSE);
        }

        root = inflater.inflate(R.layout.fragment_course_lectures, container, false);


        lecturesListView = (ListView)root.findViewById(R.id.course_lecture_listView);

        if(lecturesListView != null) {

            Log.println(Log.ASSERT,"COURSEDETAIL", "size = " + course.getLectures().size());
            lectureAdapter = new LectureAdapter(course.getLectures(), isEdit, activity);
            lecturesListView.setAdapter(lectureAdapter);
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        String tail = course.toString();
        outState.putString(BUNDLE_KEY_TAIL,tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_COURSE, course);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
