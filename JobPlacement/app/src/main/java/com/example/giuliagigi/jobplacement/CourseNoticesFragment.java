package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CourseNoticesFragment extends Fragment {

    private Course course;
    private boolean isEdit;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseNoticesFragment newInstance(Course course, boolean isEdit) {

        CourseNoticesFragment fragment = new CourseNoticesFragment();

        fragment.course = course;
        fragment.isEdit = isEdit;

        return fragment;
    }

    public CourseNoticesFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CALLBACKS ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_course_notices, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
