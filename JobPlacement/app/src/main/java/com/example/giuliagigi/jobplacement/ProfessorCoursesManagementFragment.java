package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ProfessorCoursesManagementFragment extends Fragment {

    private Professor professor;

    private View root;

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

        root = inflater.inflate(R.layout.fragment_professor_courses_management, container, false);

        TextView tv = (TextView)root.findViewById(R.id.temp_tv);

        tv.setText(tv.getText().toString() + " (" + professor.getSurname() + ")");

        return root;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
