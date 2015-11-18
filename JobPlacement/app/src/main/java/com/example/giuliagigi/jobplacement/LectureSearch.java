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

    private static final String BUNDLE_IDENTIFIER = "LECTURESEARCH";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_STUDENT = "bundle_student";


    Button display;
    AutoCompleteTextView etCourse, etProfessor;
    FrameLayout lectureDisplayContainer, courseDisplayContainer;

    private GlobalData globalData;
    private LecturesFileReader lecturesFileReader;
    private ArrayList<Course> currentCourses;
    private Student student;

    public static LectureSearch newInstance(Student student){

        LectureSearch fragment = new LectureSearch();
        fragment.student = student;
        return fragment;
    }

    public LectureSearch(){
        super();
    }


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        globalData = (GlobalData)activity.getApplicationContext();
        lecturesFileReader = globalData.getLecturesFileReader();
        currentCourses = new ArrayList<Course>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.println(Log.ASSERT, "LECTURESEARCH", "onCreate. student: " + (student == null ? "null" : student.toString()));

        if(savedInstanceState != null){

            Log.println(Log.ASSERT, "LECTURESEARCH", "restoring state");
            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null)
                student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.timetable_search, container, false);

        display = (Button)root.findViewById(R.id.displayButton);
        etCourse = (AutoCompleteTextView)root.findViewById(R.id.editTextCourse);
        etProfessor = (AutoCompleteTextView)root.findViewById(R.id.editTextProfessor);
        lectureDisplayContainer = (FrameLayout)root.findViewById(R.id.lecture_display_container);
        courseDisplayContainer = (FrameLayout)root.findViewById(R.id.course_display_container);
        ArrayAdapter<String> adapterCourses = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getCourseNames());
        etCourse.setAdapter(adapterCourses);

        ArrayAdapter<String> adapterProfessors = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getProfessorNames());
        etProfessor.setAdapter(adapterProfessors);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requestedCourse = etCourse.getText().toString();
                String requestedProfessor = etProfessor.getText().toString();

                currentCourses = lecturesFileReader.getCourses(requestedCourse,requestedProfessor);

                LectureDisplayFragment ldf = LectureDisplayFragment.newInstance(currentCourses, LectureDisplayFragment.MODE_SIMPLE_SCHEDULE);
                StudentCoursesManagementFragment scmf = StudentCoursesManagementFragment.newInstance(student,currentCourses);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.lecture_display_container, ldf).commit();

                FragmentTransaction ft2 = getActivity().getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.course_display_container, scmf).commit();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.println(Log.ASSERT, "LECTURESEARCH", "saving state");

        String tail = student.toString();
        outState.putString(BUNDLE_KEY_TAIL, tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_STUDENT, student);

        super.onSaveInstanceState(outState);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // ----------------- END STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////

}
