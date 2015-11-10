package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class StudentCoursesManagementFragment extends Fragment {
    
    private Student student;
    
    private View root;
    private RecyclerView coursesView;
    private CourseAdapter courseAdapter;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentCoursesManagementFragment newInstance(Student student) {
        
        StudentCoursesManagementFragment fragment = new StudentCoursesManagementFragment();
        fragment.student = student;
        
        return fragment;
    }

    public StudentCoursesManagementFragment() {}



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

        root =  inflater.inflate(R.layout.fragment_courses_management, container, false);

        coursesView = (RecyclerView)root.findViewById(R.id.professor_courses_recyclerView);
        coursesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        courseAdapter = new CourseAdapter(getActivity(),student.getCourses(),CourseAdapter.MODE_STUDENT_VIEW);
        coursesView.setAdapter(courseAdapter);
        
        return root;
    }

    
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
