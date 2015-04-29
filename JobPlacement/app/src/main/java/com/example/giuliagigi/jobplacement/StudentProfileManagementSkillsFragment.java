package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private Student currentUser;
    Button addDegree;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;

    /*----------------------- CONSTRUCTORS ------------------------------------------------------*/

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance() {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    /*----------------------- STANDARD CALLBACKS -------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.println(Log.ASSERT, "REGISTRY FRAG", "OnAttach");
        currentUser = application.getStudentFromUser();
        degreeFragments = new ArrayList<StudentProfileManagementDegreeFragment>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.println(Log.ASSERT,"REGISTRY FRAG", "OnCreateView");
        root = inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);

        addDegree = (Button)root.findViewById(R.id.skills_add_degree);
        addDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.println(Log.ASSERT,"SKILLS FRAG", "View is:" + v.toString() );
                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(new Degree());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_degreeList_container,dmf);
                ft.commit();
                degreeFragments.add(dmf);
            }
        });

        int max = Math.max(degreeFragments.size(),currentUser.getDegrees().size());
        for(int i=0;i<max;i++){
            Log.println(Log.ASSERT,"SKILLS FRAG", "adding fragment (existing degree)");

            if(i>=degreeFragments.size()){

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(currentUser.getDegrees().get(i));
                degreeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_degreeList_container, degreeFragments.get(i));
            ft.commit();

        }


        setEnable(host.isEditMode());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        for(Fragment f: degreeFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.println(Log.ASSERT,"REGISTRY FRAG", "OnDetach");
    }

    /*----------------------- AUXILIARY METHODS ------------------------------------------------------*/

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
    }

}
