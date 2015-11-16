package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class CourseOverviewFragment extends Fragment {

    private View root;
    private TextView code,name,professor;
    private EditText presentation, examModalities;


    private Course course;


    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseOverviewFragment newInstance(Course course) {

        CourseOverviewFragment fragment = new CourseOverviewFragment();
        fragment.course = course;
        return fragment;
    }

    public CourseOverviewFragment() {}


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- STANDARD CALLBACKS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_course_overview, container, false);

        code = (TextView)root.findViewById(R.id.course_code_textView);
        name = (TextView)root.findViewById(R.id.course_name_textView);
        professor = (TextView)root.findViewById(R.id.course_professor_textView);
        presentation = (EditText)root.findViewById(R.id.course_presentation_editText);
        examModalities = (EditText)root.findViewById(R.id.course_examModalities_editText);

        if(code != null)            code.setText(course.getCode());
        if(name != null)            name.setText(course.getName());
        if(professor != null)       professor.setText(course.getProfessor().getName() + " " + course.getProfessor().getSurname());
        if(presentation != null)    presentation.setText(course.getPresentation());
        if(examModalities != null)  examModalities.setText(course.getExam());

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}
