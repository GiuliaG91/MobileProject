package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private static final String TITLE = "Skills";

    private Student currentUser;
    Button addDegree, addLanguage,addCertificate;
    ImageButton addTag;
    GridLayout tagContainer;
    MultiAutoCompleteTextView tagsText;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;
    ArrayList<StudentProfileManagementLanguageFragment> languageFragments;
    ArrayList<StudentProfileManagementCertificateFragment> certificateFragments;
    HashMap<String,Tag> studentTags;
    ArrayList<View> tagViews;

    /*----------------------- CONSTRUCTORS ------------------------------------------------------*/

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance() {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    /*----------------------- STANDARD CALLBACKS -------------------------------------------------*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        currentUser = (Student)application.getUserObject();
        degreeFragments = new ArrayList<StudentProfileManagementDegreeFragment>();
        languageFragments = new ArrayList<StudentProfileManagementLanguageFragment>();
        certificateFragments = new ArrayList<StudentProfileManagementCertificateFragment>();
        studentTags = currentUser.getTags();
        tagViews = new ArrayList<View>();
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

        addCertificate = (Button)root.findViewById(R.id.skills_add_certificate);
        addCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StudentProfileManagementCertificateFragment cmf = StudentProfileManagementCertificateFragment.newInstance(new Certificate());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.student_certificateList_container,cmf);
                ft.commit();
                certificateFragments.add(cmf);
            }
        });



        tagsText = (MultiAutoCompleteTextView) root.findViewById(R.id.student_tagAutoComplete_text);
        addTag = (ImageButton) root.findViewById(R.id.student_addTagButton);
        tagContainer = (GridLayout)root.findViewById(R.id.student_tagContainer);

        for(String t:studentTags.keySet()){

            final View mytagView = inflater.inflate(R.layout.taglayout, null);
            final TextView tagTextView = (TextView) mytagView.findViewById(R.id.tag_tv);
            tagTextView.setText(t);

            mytagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tagContainer.removeView(v);
                    currentUser.removeTag(studentTags.get(tagTextView.getText().toString()));
                    studentTags.remove(tagTextView.getText().toString());
                    tagViews.remove(mytagView);
                    Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                    hasChanged = true;
                }
            });

            tagContainer.addView(mytagView);
            tagViews.add(mytagView);
        }


        final String[] tagNames = new String[application.getTags().size()];

        int i =0;
        for (String t: application.getTags().keySet())
            tagNames[i++] = t;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, tagNames);

        tagsText.setAdapter(adapter);
        tagsText.setTokenizer(new SpaceTokenizer());
        tagsText.setThreshold(1);

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> support = new ArrayList<String>();
                for (String t: tagNames)
                    support.add(t.toLowerCase().trim());

                ArrayList<String> existent = new ArrayList<String>();
                for(String t: studentTags.keySet())
                    existent.add(t.toLowerCase().trim());

                if(support.contains(tagsText.getText().toString().toLowerCase().trim()))
                {
                    if(!existent.contains(tagsText.getText().toString().toLowerCase().trim())) {

                        LayoutInflater inflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View mytagView = inflater.inflate(R.layout.taglayout, null);
                        final TextView t = (TextView) mytagView.findViewById(R.id.tag_tv);
                        t.setText(tagsText.getText().toString());

                        mytagView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                tagContainer.removeView(v);
                                currentUser.removeTag(studentTags.get(t.getText().toString()));
                                studentTags.remove(t.getText().toString());
                                tagViews.remove(mytagView);
                                Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
                            }
                        });

                        tagContainer.addView(mytagView);
                        tagViews.add(mytagView);
                        studentTags.put(tagsText.getText().toString(), application.getTags().get(tagsText.getText().toString()));

                        tagsText.setText("");
                        Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT ).show();
                        hasChanged = true;

                    }else Toast.makeText(getActivity().getApplicationContext(),"Existent tag",Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(getActivity().getApplicationContext(),"Wrong tag",Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

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

        max = Math.max(certificateFragments.size(),currentUser.getCertificates().size());
        for(int j=0;j<max;j++){

            if(j>=certificateFragments.size()){

                StudentProfileManagementCertificateFragment cmf = StudentProfileManagementCertificateFragment.newInstance(currentUser.getCertificates().get(j));
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
    protected void setEnable(boolean enable) {

        super.setEnable(enable);

        addTag.setEnabled(enable);
        addCertificate.setEnabled(enable);
        addDegree.setEnabled(enable);
        addLanguage.setEnabled(enable);
        tagsText.setEnabled(enable);

        for(View tag:tagViews)
            tag.setEnabled(enable);

    }

    @Override
    public void saveChanges() {
        super.saveChanges();
        Log.println(Log.ASSERT, "SKILLS FRAG", "sto salvando skills");
        Log.println(Log.ASSERT, "SKILLS FRAG", "lista tag: ");

        for(Tag t: studentTags.values()){

            Log.println(Log.ASSERT, "SKILLS FRAG", "tag: " + t.getTag());
            currentUser.addTag(t);
        }

        currentUser.saveEventually();
    }
}
