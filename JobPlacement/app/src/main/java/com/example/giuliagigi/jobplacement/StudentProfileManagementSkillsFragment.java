package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Skills";
    public static final String BUNDLE_IDENTIFIER = "STUDENTPROFILESKILLS";
    private static final String BUNDLE_KEY_STUDENT = "BUNDLE_KEY_STUDENT";

    private Student student;
    Button addDegree, addLanguage,addCertificate;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;
    ArrayList<StudentProfileManagementLanguageFragment> languageFragments;
    ArrayList<StudentProfileManagementCertificateFragment> certificateFragments;

    /*----------------------- CONSTRUCTORS ------------------------------------------------------*/

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance(Student student) {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setStudent(student);
        fragment.setUser(student);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    public void setStudent(Student student){

        this.student = student;
    }

    @Override
    public String getBundleID() {

        return BUNDLE_IDENTIFIER + ";" + getTag();
    }

    /*----------------------- STANDARD CALLBACKS -------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        degreeFragments = new ArrayList<StudentProfileManagementDegreeFragment>();
        languageFragments = new ArrayList<StudentProfileManagementLanguageFragment>();
        certificateFragments = new ArrayList<StudentProfileManagementCertificateFragment>();
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


        if(root == null)
            root = inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);

        addDegree = (Button)root.findViewById(R.id.skills_add_degree);
        addDegree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(new Degree(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_degreeList_container,dmf);
                ft.commit();
                degreeFragments.add(dmf);
            }
        });

        addLanguage = (Button)root.findViewById(R.id.skills_add_language);
        addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementLanguageFragment lmf = StudentProfileManagementLanguageFragment.newInstance(new Language(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_languageList_container,lmf);
                ft.commit();
                languageFragments.add(lmf);
            }
        });

        addCertificate = (Button)root.findViewById(R.id.skills_add_certificate);
        addCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementCertificateFragment cmf = StudentProfileManagementCertificateFragment.newInstance(new Certificate(), student);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_certificateList_container,cmf);
                ft.commit();
                certificateFragments.add(cmf);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        int max = Math.max(degreeFragments.size(), student.getDegrees().size());
        for(int i=0;i<max;i++){

            if(i>=degreeFragments.size()){

                StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(student.getDegrees().get(i), student);
                degreeFragments.add(dmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_degreeList_container, degreeFragments.get(i));
            ft.commit();

        }

        max = Math.max(languageFragments.size(), student.getLanguages().size());
        for(int j=0;j<max;j++){

            if(j>=languageFragments.size()){

                StudentProfileManagementLanguageFragment lmf = StudentProfileManagementLanguageFragment.newInstance(student.getLanguages().get(j), student);
                languageFragments.add(lmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_languageList_container, languageFragments.get(j));
            ft.commit();

        }

        max = Math.max(certificateFragments.size(), student.getCertificates().size());
        for(int j=0;j<max;j++){

            if(j>=certificateFragments.size()){

                StudentProfileManagementCertificateFragment cmf = StudentProfileManagementCertificateFragment.newInstance(student.getCertificates().get(j), student);
                certificateFragments.add(cmf);
            }

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_certificateList_container, certificateFragments.get(j));
            ft.commit();

        }

        setEnable(host.isEditMode());
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

        for(Fragment f: certificateFragments){

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(f);
            ft.commit();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(ProfileManagementFragment f: degreeFragments)
            host.removeOnActivityChangedListener(f);
        for (ProfileManagementFragment f: languageFragments)
            host.removeOnActivityChangedListener(f);
        for (ProfileManagementFragment f: certificateFragments)
            host.removeOnActivityChangedListener(f);
    }

    /*----------------------- AUXILIARY METHODS ------------------------------------------------------*/

    @Override
    protected void restoreStateFromBundle() {
        super.restoreStateFromBundle();

        if(bundle!=null)
            student = (Student)bundle.get(BUNDLE_KEY_STUDENT);
    }

    @Override
    protected void saveStateInBundle() {
        super.saveStateInBundle();

        if(bundle!=null)
            bundle.put(BUNDLE_KEY_STUDENT,student);
    }

    @Override
    protected void setEnable(boolean enable) {

        super.setEnable(enable);
        int visibility;
        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;

        addCertificate.setVisibility(visibility);
        addDegree.setVisibility(visibility);
        addLanguage.setVisibility(visibility);
    }

    @Override
    public void saveChanges() {
        super.saveChanges();
    }
}
