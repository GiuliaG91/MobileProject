package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProfessorCoursesManagementFragment extends Fragment {

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
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        Log.println(Log.ASSERT,"COURSESMANAG", "onCreateView");

        root = inflater.inflate(R.layout.fragment_courses_management, container, false);

        coursesView = (RecyclerView)root.findViewById(R.id.professor_courses_recyclerView);
        coursesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseAdapter = new CourseAdapter(getActivity(),professor.getCourses(), professor, CourseAdapter.MODE_PROFESSOR_VIEW);
        coursesView.setAdapter(courseAdapter);

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
