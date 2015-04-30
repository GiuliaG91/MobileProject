package com.example.giuliagigi.jobplacement;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private Student currentUser;
    Button addDegree;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;
    private ArrayList<EditText> languagesTexts;
    private ArrayList<String> originalLanguages;
    private Button languagePlus;
    private LinearLayout languagesContainer;

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
        languagesTexts = new ArrayList<EditText>();
        originalLanguages = new ArrayList<String>();
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

        languagesContainer = (LinearLayout)root.findViewById(R.id.student_languages_container);
        ArrayList<String> userLanguages = currentUser.getLanguages();

        for(String l: userLanguages){

            newLanguageText(l);
            originalLanguages.add(l);
        }

        languagePlus = (Button)root.findViewById(R.id.student_languages_plusButton);
        languagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newLanguageText(null);
            }
        });

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

        int visibility;

        if(enable)
            visibility = View.VISIBLE;
        else
            visibility = View.INVISIBLE;
        super.setEnable(enable);

        Button languagePlus = (Button)root.findViewById(R.id.student_languages_plusButton);
        languagePlus.setVisibility(visibility);

        for(EditText et: languagesTexts){
            et.setEnabled(enable);
        }
        updateLanguages();
        currentUser.saveInBackground();
    }


    private void newLanguageText(String language){

        EditText newLanguage = new EditText(getActivity().getApplicationContext());
        newLanguage.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

        if(language == null)
            newLanguage.setHint("new language");
        else
            newLanguage.setText(language);

        newLanguage.setTextColor(Color.parseColor("#000000"));
        newLanguage.addTextChangedListener(new OnFieldChangedListener());
        languagesContainer.addView(newLanguage);
        languagesTexts.add(newLanguage);
    }

    private void updateLanguages(){


        for(int i = 0;i<languagesTexts.size();i++){

            String language = languagesTexts.get(i).getText().toString();

            if(i<originalLanguages.size() && !originalLanguages.get(i).equals(language)){

                Log.println(Log.ASSERT,"REG FRAG", "older: " + originalLanguages.get(i) + ", new: " + language);
                currentUser.removeLanguage(originalLanguages.get(i));
                currentUser.saveInBackground();
            }

            if(!language.equals("")){


                currentUser.addLanguage(language);
                currentUser.saveInBackground();
            }
            else {

                languagesContainer.removeView(languagesTexts.get(i));
                languagesTexts.remove(i);
            }
        }

        originalLanguages = new ArrayList<String>();

        for(EditText et:languagesTexts)
            originalLanguages.add(et.getText().toString());
    }

}
