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
    Button addLanguage;
    ArrayList<StudentProfileManagementLanguageFragment> languageFragments;

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
        currentUser = (Student)application.getUserObject();
        degreeFragments = new ArrayList<StudentProfileManagementDegreeFragment>();
        languageFragments = new ArrayList<StudentProfileManagementLanguageFragment>();
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

        root = inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);

        addDegree = (Button)root.findViewById(R.id.skills_add_degree);
        addDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(new Degree());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_degreeList_container,dmf);
                ft.commit();
                degreeFragments.add(dmf);
            }
        });

        int max = Math.max(degreeFragments.size(),currentUser.getDegrees().size());
        for(int i=0;i<max;i++){

            if(i>=degreeFragments.size()){

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(currentUser.getDegrees().get(i));
                degreeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_degreeList_container, degreeFragments.get(i));
            ft.commit();

        }

        addLanguage = (Button)root.findViewById(R.id.skills_add_language);
        addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementLanguageFragment lmf = StudentProfileManagementLanguageFragment.newInstance(new Language());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_languageList_container,lmf);
                ft.commit();
                languageFragments.add(lmf);
            }
        });

        max = Math.max(languageFragments.size(),currentUser.getLanguages().size());
        for(int j=0;j<max;j++){

            if(j>=languageFragments.size()){

                StudentProfileManagementLanguageFragment lmf = StudentProfileManagementLanguageFragment.newInstance(currentUser.getLanguages().get(j));
                languageFragments.add(lmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_languageList_container, languageFragments.get(j));
            ft.commit();

        }
        setEnable(host.isEditMode());
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        for(Fragment f: degreeFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

        for(Fragment f: languageFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.println(Log.ASSERT,"REGISTRY FRAG", "OnDetach");

        for(ProfileManagementFragment f: degreeFragments)
            host.removeOnActivityChangedListener(f);
        for (ProfileManagementFragment f: languageFragments)
            host.removeOnActivityChangedListener(f);
    }

    /*----------------------- AUXILIARY METHODS ------------------------------------------------------*/

    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }
}
