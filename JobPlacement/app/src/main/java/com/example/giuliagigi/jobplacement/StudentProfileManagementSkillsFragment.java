package com.example.giuliagigi.jobplacement;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StudentProfileManagementSkillsFragment extends Fragment {

    private OnInteractionListener hostActivity;

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
        try {
            hostActivity = (OnInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnInteractionListener");
        }
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
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_student_profile_management_skills, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnInteractionListener {


    }

}
