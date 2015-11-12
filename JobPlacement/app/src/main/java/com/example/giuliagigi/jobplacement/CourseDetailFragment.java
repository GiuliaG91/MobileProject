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


public class CourseDetailFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "COURSEDETAIL";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_COURSE = "bundle_course";


    private View root;
    private TextView code,name,professor;
    private ListView lecturesListView;
    private LectureAdapter lectureAdapter;

    private GlobalData globalData;
    private Course course;
    private boolean isEdit;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseDetailFragment newInstance(Course course, boolean isEdit) {

        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.course = course;
        fragment.course.cacheData();
        fragment.isEdit = isEdit;

        return fragment;
    }

    public CourseDetailFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

        root = inflater.inflate(R.layout.fragment_course_detail, container, false);

        lecturesListView = (ListView)root.findViewById(R.id.course_lecture_listView);
        code = (TextView)root.findViewById(R.id.course_code_textView);
        name = (TextView)root.findViewById(R.id.course_name_textView);
        professor = (TextView)root.findViewById(R.id.course_professor_textView);

        LinearLayout layout = (LinearLayout)root;
        if(isEdit){

            layout.removeView(professor);
            professor = null;
        }

        if(lecturesListView != null) {

            Log.println(Log.ASSERT,"COURSEDETAIL", "size = " + course.getLectures().size());
            lectureAdapter = new LectureAdapter(course.getLectures(), isEdit);
            lecturesListView.setAdapter(lectureAdapter);
        }

        if(code != null)        code.setText(course.getCode());
        if(name != null)        name.setText(course.getName());
        if(professor != null)   professor.setText(course.getProfessor().getName() + " " + course.getProfessor().getSurname());


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
