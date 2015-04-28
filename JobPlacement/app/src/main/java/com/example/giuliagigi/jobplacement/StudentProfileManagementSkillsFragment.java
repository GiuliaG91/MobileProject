package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.giuliagigi.jobplacement.R;
import java.util.ArrayList;

public class StudentProfileManagementSkillsFragment extends ProfileManagementFragment {

    private Student currentUser;
    Button addDegree;
    ArrayList<StudentProfileManagementDegreeFragment> degreeFragments;

    public StudentProfileManagementSkillsFragment() {super();}
    public static StudentProfileManagementSkillsFragment newInstance() {
        StudentProfileManagementSkillsFragment fragment = new StudentProfileManagementSkillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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


        for(Degree d: currentUser.getDegrees()){
            Log.println(Log.ASSERT,"SKILLS FRAG", "adding fragment (existing degree)");
            StudentProfileManagementDegreeFragment dmf = StudentProfileManagementDegreeFragment.newInstance(d);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.student_degreeList_container,dmf);
            ft.commit();
            degreeFragments.add(dmf);
        }


        setEnable(host.isInEditMode());
        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    protected void setEnable(boolean enable) {
        super.setEnable(enable);
    }

}
