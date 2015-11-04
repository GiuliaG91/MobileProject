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
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * Created by GiuliaGiGi on 25/10/15.
 */
public class LectureSearch extends Fragment {

    private static final String TAG = "Main activity - LOG: ";

    Button display;
    AutoCompleteTextView etCourse, etProfessor;
    FrameLayout lectureDisplayContainer;
    private LecturesFileReader lecturesFileReader;

    public static LectureSearch newInstance(){

        return new LectureSearch();
    }

    public LectureSearch(){}


    //////////////////////////////////////////////////////////////////////////////////////
    // --------------------- STANDARD METHODS ------------------------------------------//
    //////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        GlobalData app = (GlobalData)activity.getApplicationContext();
        lecturesFileReader = app.getLecturesFileReader();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.timetable_search, container, false);

        display = (Button)root.findViewById(R.id.displayButton);
        etCourse = (AutoCompleteTextView)root.findViewById(R.id.editTextCourse);
        etProfessor = (AutoCompleteTextView)root.findViewById(R.id.editTextProfessor);
        lectureDisplayContainer = (FrameLayout)root.findViewById(R.id.lecture_display_container);

        ArrayAdapter<String> adapterCourses = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getAutocompleteCourses());
        etCourse.setAdapter(adapterCourses);


        ArrayAdapter<String> adapterProfessors = new ArrayAdapter<String>(GlobalData.getContext(),R.layout.row_spinner, lecturesFileReader.getAutocompleteProfessors());
        etProfessor.setAdapter(adapterProfessors);

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String requestedCourse = etCourse.getText().toString();

                String requestedProfessor = etProfessor.getText().toString();

                Log.println(Log.ASSERT, TAG, "Recovering information from the model");
                Log.println(Log.ASSERT, TAG, "Requested course: " + requestedCourse);
                Log.println(Log.ASSERT, TAG, "Requested professor: " + requestedProfessor);

                LectureDisplayFragment ldf = LectureDisplayFragment.newInstance(requestedCourse,requestedProfessor);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
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



    // old method
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.timetable_search);
//
//        final Button b = (Button)findViewById(R.id.displayButton);
//        etCourse = (EditText)findViewById(R.id.editTextCourse);
//        EditText etProfessor = (EditText)findViewById(R.id.editTextProfessor);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String requestedCourse = etCourse.getText().toString();
//                String requestedProfessor = etProfessor.getText().toString();
//
//                Log.println(Log.ASSERT, TAG, "Recovering information from the model");
//                Log.println(Log.ASSERT, TAG, "Requested course: " + requestedCourse);
//                Log.println(Log.ASSERT, TAG, "Requested professor: " + requestedProfessor);
//
//                Intent i = new Intent(getApplicationContext(),LectureDisplayFragment.class);
//                boolean isCourseRequested = false, isProfessorRequested = false;
//
//                if(!requestedCourse.trim().isEmpty())       isCourseRequested = true;
//                if(!requestedProfessor.trim().isEmpty())    isProfessorRequested = true;
//
//                i.putExtra(GlobalData.BUNDLE_KEY_CONTAINS_COURSE_NAME,isCourseRequested);
//                i.putExtra(GlobalData.BUNDLE_KEY_CONTAINS_PROFESSOR_NAME,isProfessorRequested);
//
//                i.putExtra(GlobalData.BUNDLE_KEY_COURSE_NAME, requestedCourse);
//                i.putExtra(GlobalData.BUNDLE_KEY_PROFESSOR_NAME, requestedProfessor);
//                startActivity(i);
//            }
//        });
//
//
//        etCourse.addTextChangedListener(new TextWatcher() {
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                b.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());
//            }
//        });
//
//        etProfessor.addTextChangedListener(new TextWatcher() {
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//            public void afterTextChanged(Editable s) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                b.setEnabled(!etCourse.getText().toString().trim().isEmpty() || !etProfessor.getText().toString().trim().isEmpty());
//            }
//        });
//
//    }




}