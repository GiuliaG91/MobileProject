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

import java.util.ArrayList;


public class StudentCoursesManagementFragment extends Fragment {
    
    private Student student;
    private ArrayList<Course> courses;

    private View root;
    private RecyclerView coursesView;
    private CourseAdapter courseAdapter;

    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- CONSTRUCTOR ----------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static StudentCoursesManagementFragment newInstance(Student student) {

        StudentCoursesManagementFragment fragment = new StudentCoursesManagementFragment();
        fragment.student = student;
        fragment.courses = null;

        return fragment;
    }


    public static StudentCoursesManagementFragment newInstance(Student student, ArrayList<Course> courses) {

        StudentCoursesManagementFragment fragment = new StudentCoursesManagementFragment();
        fragment.student = student;
        fragment.courses = courses;

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

        if(student != null)
            courseAdapter = new CourseAdapter(getActivity(),student.getCourses(), student, CourseAdapter.MODE_STUDENT_VIEW);

        if(courses != null){

            Log.println(Log.ASSERT,"COURSEMANAG", "adapter for add course");
            courseAdapter = new CourseAdapter(getActivity(),courses, student, CourseAdapter.MODE_STUDENT_ADD);

            // necessary when recycler view is nested inside a scrollview
            float height = courses.size()*getActivity().getApplicationContext().getResources().getDimension(R.dimen.course_item_row_height) + (courses.size())*getActivity().getApplicationContext().getResources().getDimension(R.dimen.course_item_row_margin);
            coursesView.getLayoutParams().height = (int)height;
        }

        coursesView.setAdapter(courseAdapter);
        return root;
    }

    
    @Override
    public void onDetach() {
        super.onDetach();
    }

}
