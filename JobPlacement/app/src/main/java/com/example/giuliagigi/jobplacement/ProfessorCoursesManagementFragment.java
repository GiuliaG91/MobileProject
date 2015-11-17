package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfessorCoursesManagementFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "PROFESSORCOURSESMANAGEMENT";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_PROFESSOR = "bundle_professor";

    private GlobalData globalData;
    private Professor professor;

    private View root;
    private RecyclerView coursesView;
    private CourseAdapter courseAdapter;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/


    public static ProfessorCoursesManagementFragment newInstance(Professor professor) {

        ProfessorCoursesManagementFragment fragment = new ProfessorCoursesManagementFragment();
        fragment.professor = professor;
        return fragment;
    }

    public ProfessorCoursesManagementFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        globalData = (GlobalData)activity.getApplicationContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

            Log.println(Log.ASSERT,"PROFCOURSESMANAG", "onRestoreInstanceState");

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null){

                professor = (Professor)bundle.get(BUNDLE_KEY_PROFESSOR);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_courses_management, container, false);

        coursesView = (RecyclerView)root.findViewById(R.id.professor_courses_recyclerView);
        coursesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseAdapter = new CourseAdapter(getActivity(), professor.getCourses(), professor, CourseAdapter.MODE_PROFESSOR_VIEW);
        coursesView.setAdapter(courseAdapter);

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.println(Log.ASSERT,"PROFCOURSESMANAG", "onSaveInstanceState");

        String tail = professor.toString();
        outState.putString(BUNDLE_KEY_TAIL, tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_PROFESSOR, professor);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
