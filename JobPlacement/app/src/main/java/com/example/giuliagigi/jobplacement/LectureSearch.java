package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class LectureSearch extends Fragment {

    private static final String TAG = "LECTURESEARCH";


    Button display;
    AutoCompleteTextView etCourse, etProfessor;
    FrameLayout lectureDisplayContainer;

    private LecturesFileReader lecturesFileReader;
    private ArrayList<Course> currentCourses;
    private Student student;

    public static LectureSearch newInstance(Student student){

        LectureSearch fragment = new LectureSearch();
        fragment.student = student;
        return fragment;
    }

    public LectureSearch(){}


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.println(Log.ASSERT,"LECTURESEARCH", "onAttach");
        GlobalData app = (GlobalData)activity.getApplicationContext();
        lecturesFileReader = app.getLecturesFileReader();
        currentCourses = new ArrayList<Course>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.println(Log.ASSERT,"LECTURESEARCH", "onCreateView");

        View root = inflater.inflate(R.layout.timetable_search, container, false);

        display = (Button)root.findViewById(R.id.displayButton);
        etCourse = (AutoCompleteTextView)root.findViewById(R.id.editTextCourse);
        etProfessor = (AutoCompleteTextView)root.findViewById(R.id.editTextProfessor);
        lectureDisplayContainer = (FrameLayout)root.findViewById(R.id.lecture_display_container);

        ArrayAdapter<String> adapterCourses = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getCourseNames());
        etCourse.setAdapter(adapterCourses);


        ArrayAdapter<String> adapterProfessors = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getProfessorNames());
        etProfessor.setAdapter(adapterProfessors);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requestedCourse = etCourse.getText().toString();

                String requestedProfessor = etProfessor.getText().toString();

                Log.println(Log.ASSERT, TAG, "Recovering information from the model");
                Log.println(Log.ASSERT, TAG, "Requested course: " + requestedCourse);
                Log.println(Log.ASSERT, TAG, "Requested professor: " + requestedProfessor);

                currentCourses = lecturesFileReader.getCourses(requestedCourse,requestedProfessor);
                LectureDisplayFragment ldf = LectureDisplayFragment.newInstance(currentCourses);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.lecture_display_container, ldf);
                ft.commit();

            }
        });


        etCourse.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                display.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());

                if(lecturesFileReader.getProfessorByCourse(etCourse.getText().toString())!=null){
                    etProfessor.setText(lecturesFileReader.getProfessorByCourse(etCourse.getText().toString()));
                }

            }
        });

        etProfessor.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                display.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());
            }
        });

        return root;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

}
