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

    private static final String BUNDLE_IDENTIFIER = "STUDENTCOURSESMANAG";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_STUDENT = "bundle_student";
    private static final String BUNDLE_KEY_COURSES = "bundle_courses";

    GlobalData globalData;
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
        globalData = (GlobalData)activity.getApplicationContext();
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null){

                student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
                ArrayList list = bundle.getList(BUNDLE_KEY_COURSES);
                if(list != null){

                    courses = new ArrayList<>();
                    for(Object o : list)
                        courses.add((Course)o);
                }

            }
        }
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.fragment_courses_management, container, false);

        coursesView = (RecyclerView)root.findViewById(R.id.professor_courses_recyclerView);
        coursesView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(courses == null){

            courseAdapter = new CourseAdapter(getActivity(), student.getCourses(), student, CourseAdapter.MODE_STUDENT_VIEW);
        }
        else {

            courseAdapter = new CourseAdapter(getActivity(),courses, student, CourseAdapter.MODE_STUDENT_ADD);

            // necessary when recycler view is nested inside a scrollview
            float height = courses.size()*getActivity().getApplicationContext().getResources().getDimension(R.dimen.course_item_row_height) + (courses.size())*getActivity().getApplicationContext().getResources().getDimension(R.dimen.course_item_row_margin);
            coursesView.getLayoutParams().height = (int)height;
        }

        coursesView.setAdapter(courseAdapter);
        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        String tail = student.toString();
        if(courses != null) tail += courses.toString();
        outState.putString(BUNDLE_KEY_TAIL, tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_STUDENT,student);
        bundle.putList(BUNDLE_KEY_COURSES,courses);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
