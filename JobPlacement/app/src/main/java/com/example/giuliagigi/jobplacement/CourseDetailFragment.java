package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CourseDetailFragment extends Fragment {

    private View root;
    private Course course;
    private boolean isEdit;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseDetailFragment newInstance(Course course, boolean isEdit) {

        CourseDetailFragment fragment = new CourseDetailFragment();
        fragment.course = course;
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
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_course_detail, container, false);

        TextView tv = (TextView)root.findViewById(R.id.temp_textView);
        tv.setText(tv.getText().toString() + " of " + course.getName() + " (" + isEdit + ")");

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
