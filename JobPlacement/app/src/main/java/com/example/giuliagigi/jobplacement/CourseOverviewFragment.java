package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CourseOverviewFragment extends Fragment {

    private static final String BUNDLE_IDENTIFIER = "COURSEOVERVIEW";
    private static final String BUNDLE_KEY_TAIL = "bundle_tail";
    private static final String BUNDLE_KEY_COURSE = "bundle_course";
    private static final String BUNDLE_KEY_PRESENTATION = "bundle_presentation";
    private static final String BUNDLE_KEY_EXAM = "bundle_exam";
    private static final String BUNDLE_KEY_EDITABLE = "bundle_editable";
    private static final String BUNDLE_KEY_EDIT = "bundle_is_edit";

    private View root;
    private TextView code,name,professor;
    private EditText presentation, examModalities;
    private Button modify;

    private String savedPresentation = null, savedExam = null;

    private GlobalData globalData;
    private Course course;
    private boolean isEditMode, editable;

    /* -------------------------------------------------------------------------------------------*/
    /* ---------------------- CONSTRUCTORS GETTERS SETTERS ---------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    public static CourseOverviewFragment newInstance(Course course, boolean editable) {

        CourseOverviewFragment fragment = new CourseOverviewFragment();
        fragment.course = course;
        fragment.editable = editable;
        fragment.isEditMode = false;
        return fragment;
    }

    public CourseOverviewFragment() {}


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

        savedPresentation = null;
        savedExam = null;

        if(savedInstanceState != null){

            Log.println(Log.ASSERT, "COURSEOVERVIEW", "onRestoreInstanceState");

            String tail = savedInstanceState.getString(BUNDLE_KEY_TAIL);
            MyBundle bundle = globalData.getBundle(BUNDLE_IDENTIFIER + tail);

            if(bundle != null){

                course = (Course)bundle.get(BUNDLE_KEY_COURSE);
                editable = bundle.getBoolean(BUNDLE_KEY_EDITABLE);
                isEditMode = bundle.getBoolean(BUNDLE_KEY_EDIT);
                savedPresentation = bundle.getString(BUNDLE_KEY_PRESENTATION);
                savedExam = bundle.getString(BUNDLE_KEY_EXAM);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_course_overview, container, false);

        code = (TextView)root.findViewById(R.id.course_code_textView);
        name = (TextView)root.findViewById(R.id.course_name_textView);
        professor = (TextView)root.findViewById(R.id.course_professor_textView);
        presentation = (EditText)root.findViewById(R.id.course_presentation_editText);
        examModalities = (EditText)root.findViewById(R.id.course_examModalities_editText);
        modify = (Button)root.findViewById(R.id.course_modify_button);

        if(code != null)            code.setText(course.getCode());
        if(name != null)            name.setText(course.getName());
        if(professor != null)       professor.setText(course.getProfessor().getName() + " " + course.getProfessor().getSurname());
        if(presentation != null)    presentation.setText(savedPresentation == null ? course.getPresentation() : savedPresentation);
        if(examModalities != null)  examModalities.setText(savedExam == null ? course.getExam() : savedExam);

        LinearLayout l = (LinearLayout)root;

        if(!editable){

            l.removeView(modify);
            modify = null;
        }

        if(modify != null){

            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isEditMode = !isEditMode;
                    setEnable(isEditMode);

                    if(!isEditMode)
                        saveChanges();
                }
            });
        }

        setEnable(isEditMode);

        return root;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {


        Log.println(Log.ASSERT, "COURSEOVERVIEW", "onSaveInstanceState");

        String tail = course.toString();
        outState.putString(BUNDLE_KEY_TAIL, tail);

        MyBundle bundle = globalData.addBundle(BUNDLE_IDENTIFIER + tail);
        bundle.put(BUNDLE_KEY_COURSE, course);
        bundle.putString(BUNDLE_KEY_PRESENTATION, presentation.getText().toString());
        bundle.putString(BUNDLE_KEY_EXAM, examModalities.getText().toString());
        bundle.putBoolean(BUNDLE_KEY_EDIT, isEditMode);
        bundle.putBoolean(BUNDLE_KEY_EDITABLE, editable);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /* -------------------------------------------------------------------------------------------*/
    /* -------------------------------- AUXIALIRY ------------------------------------------------*/
    /* -------------------------------------------------------------------------------------------*/

    private void setEnable(boolean enable){

        presentation.setEnabled(enable);
        examModalities.setEnabled(enable);

        if(modify != null)
            modify.setBackgroundResource(enable ? R.drawable.ic_save : R.drawable.ic_pencil);
    }

    private void saveChanges(){

        course.setPresentation(presentation.getText().toString());
        course.setExam(examModalities.getText().toString());
        course.saveEventually();
    }
}
